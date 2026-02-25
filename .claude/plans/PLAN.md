# Mubble — Implementation Roadmap

> Last updated: 2026-02-25
> Goal: ship a fully working social media app backed by Supabase.

---

## Backend decision: Supabase ✓

**Why Supabase over Firebase:**
- PostgreSQL — relational model is natural for social data (followers, likes, comments, circles)
- Built-in Auth (JWT, OAuth providers, magic links) — no extra library
- Realtime subscriptions over WebSocket — perfect for live chat
- Storage buckets — images, videos, avatars
- Row Level Security (RLS) — fine-grained per-user access control
- Android SDK (`io.github.jan-tennert.supabase:supabase-kt`) with Ktor transport
- Auto-generated REST + type-safe Kotlin client from schema
- Generous free tier (500 MB DB, 1 GB storage, 50k MAU)

---

## Phases at a glance

| # | Phase | What it produces |
|---|-------|-----------------|
| 1 | UI completion | Every screen is visually done, no dead ends |
| 2 | Dummy-data wiring | Full navigation + feature flows work offline |
| 3 | Supabase setup | Schema, auth, storage, realtime configured |
| 4 | Android ↔ Supabase | Real data replaces dummy data |
| 5 | Polish + launch prep | Caching, notifications, performance, testing |

---

## Phase 1 — UI Completion

Fix every screen that is visually incomplete before touching any logic.
Do these in order (top = unblocks other phases).

### 1.1 Activity screen (currently missing entirely)
- [ ] Create `feature/activity/` package with `ActivityScreen.kt`, `ActivityNavigation.kt`, `ActivityViewModel.kt`
- [ ] Design: tabbed layout — **All / Follows / Likes / Comments** tabs
- [ ] Each row: avatar + description text + relative timestamp + optional post thumbnail
- [ ] Wire bottom-nav icon to the new screen (replace current empty destination)

### 1.2 New Post screen (currently a single preview composable)
- [ ] Full modal/sheet flow: choose type → **Bubble** (text only) | **Image post** | **Video post**
- [ ] Bubble composer: expandable text field, circle selector, audience picker
- [ ] Image/video post: media picker placeholder → caption → post button
- [ ] FAB on Home navigates here; also wire the `+` action in the bottom nav
- [ ] ViewModel + `NewPostUiState` (loading / success / error) — write dummy submit for now

### 1.3 Chat Details screen (tab content commented out)
- [ ] Implement the `TabbedPager` with three tabs: **Media / Posts / Bubbles**
- [ ] Each tab: grid/list of dummy content already available in other screens
- [ ] Shared element transition from `ChatScreen` top-bar tap

### 1.4 Sign-up step 4 — Circle discovery
- [ ] Uncomment/restore `CircleItem` components
- [ ] Show a horizontal chip-filter row (category tags) + scrollable circle grid
- [ ] Selection state: tap to toggle join, continue button enables when ≥ 1 selected (or skip)

### 1.5 Minor polish pass
- [ ] Pitch-black theme variant (pure `#000000` background for OLED)
- [ ] Dynamic color toggle in Settings → Appearance (use `DynamicTheme` from Material3)
- [ ] Custom chat bubble color themes (stored in DataStore per conversation)
- [ ] Shared animated container transitions on post cards in Home/Explore (already partially done — verify and complete)
- [ ] Image picker inside ChatScreen input bar (Gallery intent or `ActivityResultContracts.PickVisualMedia`)

---

## Phase 2 — Full Dummy-data Wiring

Every feature must be navigable and interactive using the existing mock repositories before any real server work begins. This surfaces navigation bugs and missing ViewModels early.

### 2.1 Home
- [ ] "Create circle" button → navigates to a `NewCircleScreen` stub (name + description + image)
- [ ] Post like / comment counts are reactive (toggle like updates local StateFlow)
- [ ] Tapping a circle chip filters the "My Circles" feed correctly

### 2.2 Explore
- [ ] Search bar actually filters the dummy post/bubble/circle lists
- [ ] Tapping a circle → `CircleScreen`; tapping a post → `PostDetailsScreen`

### 2.3 Chats
- [ ] Sending a message appends it to the local list in `ChatViewModel`
- [ ] Delete swipe in `ChatListScreen` removes the item from StateFlow
- [ ] Long-press message → copy / delete options (bottom sheet)

### 2.4 Profile & Follow
- [ ] Follow/unfollow button on `OtherUserProfileScreen` toggles state in `UserRepo`
- [ ] Follower/following counts update accordingly
- [ ] Edit profile form submission updates `UserRepo` and pops the back stack

### 2.5 Videos
- [ ] Like / save / comment actions update local state (already mostly done)
- [ ] Comment bottom sheet posts a dummy comment and appends it to the list

### 2.6 Auth flow
- [ ] Login form validates email format + non-empty password before enabling the button
- [ ] "Forgot password" → stub dialog ("Email sent")
- [ ] Sign-up 5-step flow completes and navigates to the main app
- [ ] Logout in Settings navigates back to Login and clears back stack

---

## Phase 3 — Supabase Setup

Do all of this in the Supabase dashboard + SQL editor before touching Android code.

### 3.1 Project creation
- [ ] Create Supabase project, note `URL` and `anon key`
- [ ] Enable Email auth provider; configure password policy

### 3.2 Database schema

Run migrations in order. Core tables:

```sql
-- Users / profiles (extends auth.users)
profiles (id uuid PK → auth.users, username text unique, display_name, bio, avatar_url, banner_url, created_at)

-- Social graph
follows (follower_id uuid, following_id uuid, created_at)  -- composite PK

-- Circles (groups)
circles (id uuid PK, name, description, avatar_url, banner_url, owner_id uuid → profiles, is_public bool, created_at)
circle_members (circle_id, user_id, role text default 'member', joined_at)  -- composite PK

-- Posts and bubbles
posts (id uuid PK, author_id uuid → profiles, circle_id uuid nullable → circles,
       content text, media_urls text[], type text CHECK('post','bubble'), created_at, updated_at)

-- Engagement
likes (post_id uuid → posts, user_id uuid → profiles, created_at)  -- composite PK
comments (id uuid PK, post_id uuid → posts, author_id uuid → profiles,
          parent_id uuid nullable → comments, content text, created_at)
comment_likes (comment_id uuid, user_id uuid)  -- composite PK

-- Chat
conversations (id uuid PK, is_group bool default false, name text nullable, created_at)
conversation_members (conversation_id uuid, user_id uuid, joined_at)
messages (id uuid PK, conversation_id uuid → conversations, sender_id uuid → profiles,
          content text nullable, media_url text nullable, type text CHECK('text','image','video','post_share'),
          created_at, read_by uuid[])

-- Activity / notifications
notifications (id uuid PK, recipient_id uuid → profiles, actor_id uuid → profiles,
               type text CHECK('follow','like','comment','mention'), entity_id uuid, read bool default false, created_at)

-- Short-form video
videos (id uuid PK, author_id uuid → profiles, url text, thumbnail_url text,
        caption text, like_count int default 0, created_at)
```

### 3.3 Row Level Security policies
- [ ] `profiles`: anyone can read; only owner can update
- [ ] `posts`: public posts readable by all; circle posts readable only by circle members
- [ ] `messages`: readable only by `conversation_members`
- [ ] `notifications`: readable only by `recipient_id = auth.uid()`
- [ ] `follows` / `likes` / `comments`: readable publicly; insert/delete only by `auth.uid()`

### 3.4 Storage buckets
- [ ] `avatars` — public read, authenticated write, 2 MB limit, images only
- [ ] `banners` — same policy as avatars
- [ ] `post-media` — public read, authenticated write, 50 MB limit, images + videos
- [ ] `videos` — public read, authenticated write, 500 MB limit, video only
- [ ] `chat-media` — private read (RLS), authenticated write

### 3.5 Realtime
- [ ] Enable realtime on `messages` table (INSERT event)
- [ ] Enable realtime on `notifications` table (INSERT event)
- [ ] Enable realtime on `likes` table (INSERT / DELETE) — for live like counts

### 3.6 Edge Functions (optional but useful early)
- [ ] `on-new-follower` — insert notification row when a follow row is created (DB trigger or Edge Function)
- [ ] `on-new-like` — insert notification row on like insert

---

## Phase 4 — Android ↔ Supabase Integration

Add the Supabase Kotlin SDK and replace dummy repositories one at a time.

### 4.1 Add dependencies

In `app/build.gradle.kts`:
```kotlin
// Supabase BOM — pin to a single version
implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
implementation("io.github.jan-tennert.supabase:postgrest-kt")
implementation("io.github.jan-tennert.supabase:auth-kt")
implementation("io.github.jan-tennert.supabase:realtime-kt")
implementation("io.github.jan-tennert.supabase:storage-kt")
// Ktor engine for Android
implementation("io.ktor:ktor-client-okhttp:3.1.0")
```

Store URL + anon key in `local.properties` (never commit), inject via `BuildConfig`.

### 4.2 Hilt module for Supabase client

`di/SupabaseModule.kt` — provides a singleton `SupabaseClient` with Auth, Postgrest, Realtime, Storage plugins installed.

### 4.3 Auth (replace stub login/signup)
- [ ] `LoginViewModel`: call `supabase.auth.signInWith(Email)`, store session
- [ ] `SignUpViewModel`: `supabase.auth.signUpWith(Email)` → create `profiles` row on success
- [ ] Email verification step wired to actual Supabase email confirmation
- [ ] Session persistence: use `supabase.auth.SessionManager` with DataStore
- [ ] Auto-navigate to main app if valid session exists on cold start

### 4.4 Feed / Posts
- [ ] `PostRepository` interface updated; `SupabasePostRepository` implements it
- [ ] Fetch posts with join on `profiles` (author info), ordered by `created_at desc`
- [ ] Pagination with Postgrest `.range(from, to)` — load 20 at a time
- [ ] Like toggle: upsert/delete `likes` row + optimistic UI update

### 4.5 Explore
- [ ] Full-text search: Postgrest `.textSearch("content", query)` on posts + bubbles
- [ ] Circles search: `.ilike("name", "%$query%")`

### 4.6 Chat — Realtime
- [ ] On open `ChatScreen`: subscribe to `messages` channel filtered by `conversation_id`
- [ ] Send message: insert row → local StateFlow updated immediately (optimistic)
- [ ] Unread count: query messages where `auth.uid() NOT IN read_by` per conversation
- [ ] Mark read: array_append `read_by` on first visible

### 4.7 Profiles + Follow
- [ ] Fetch profile by `id` or `username`
- [ ] Follow: insert `follows` row; unfollow: delete it
- [ ] Follower/following counts: computed via `count()` query or DB view

### 4.8 Notifications / Activity
- [ ] Subscribe to `notifications` realtime channel on app start
- [ ] Show badge on Activity bottom-nav icon when unread count > 0
- [ ] Mark all read on screen open

### 4.9 Media upload
- [ ] Avatar upload: compress bitmap → upload to `avatars/{userId}.jpg` → update `profiles.avatar_url`
- [ ] Post media: pick image/video → upload to `post-media/{uuid}` → include URL in post insert
- [ ] Use `Coil` (already in project) with Supabase public URLs

### 4.10 Videos
- [ ] Paginate `videos` table, feed into existing ExoPlayer pager
- [ ] Upload video: pick file → upload to `videos/` bucket → insert `videos` row

---

## Phase 5 — Polish, Caching & Launch Prep

### 5.1 Offline caching
- [ ] Add Room database for local post/profile cache
- [ ] Repository pattern: emit cached data first, then refresh from Supabase
- [ ] Cache invalidation: purge posts > 7 days old

### 5.2 Push notifications
- [ ] Integrate Firebase Cloud Messaging (FCM) — send token to Supabase `profiles` table
- [ ] Supabase Edge Function triggers FCM when a notification row is inserted

### 5.3 Performance
- [ ] Profile Home/Explore scroll with `Compose compiler metrics` — fix unnecessary recompositions
- [ ] Video prefetch: preload next 1–2 videos in ExoPlayer `MediaSource` queue
- [ ] Image lazy loading: ensure `AsyncImage` uses memory + disk cache (Coil default)

### 5.4 Testing
- [ ] Unit tests for all ViewModels (use `kotlinx-coroutines-test`, `Turbine` for StateFlow)
- [ ] Integration tests for repository layer with Supabase test project
- [ ] UI tests for auth flow with Compose testing API

### 5.5 Security hardening
- [ ] Audit all RLS policies — no data leaks between users
- [ ] Validate media MIME types server-side in Storage policies
- [ ] Rate-limit sign-up in Supabase Auth dashboard
- [ ] Obfuscate `BuildConfig` keys with `secrets-gradle-plugin`

---

## Dependency graph (what blocks what)

```
Phase 1 (UI)
    └── Phase 2 (Dummy wiring)
            └── Phase 3 (Supabase schema)
                    └── Phase 4 (Integration)
                            ├── 4.3 Auth      ← must be first in Phase 4
                            ├── 4.4 Feed      ← needs auth
                            ├── 4.6 Chat      ← needs auth + realtime
                            └── 4.9 Media     ← needs storage buckets
                    └── Phase 5 (Polish)
```

---

## Suggested weekly order (solo dev pace)

| Week | Tasks |
|------|-------|
| 1 | 1.1 Activity screen, 1.2 New Post screen |
| 2 | 1.3 Chat Details, 1.4 Sign-up step 4, 1.5 minor polish |
| 3 | Phase 2 — all dummy-data wiring |
| 4 | Phase 3 — Supabase project + full schema + RLS + storage |
| 5 | 4.1–4.3 SDK setup + auth |
| 6 | 4.4–4.5 Feed + Explore |
| 7 | 4.6 Realtime chat |
| 8 | 4.7–4.10 Profiles, Activity, Media, Videos |
| 9+ | Phase 5 — caching, notifications, testing, hardening |
