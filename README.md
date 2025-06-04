# Mubble App

**Mubble** is a work in progress social media app built with **Kotlin & Jetpack Compose**. It tries following best practices from the official Android [**architecture guidance**](https://developer.android.com/topic/architecture).

The app’s intention is to become a live working example of a modern social media app, with real-world architecture and design considerations, packed with features, and hopefully serving as a useful reference for other developers.

# Features
Main app features include a **home feed** to see posts from people you follow and groups (circles) you’re part of, with the ability to join and create groups; posting **photos**, **videos**, or **text-only posts (bubbles)**; an **explore page** to discover trending content and posts matching your interests; a **chat screen** to message friends, send photos, and share posts; an **activity tab** showing updates like new follows, likes, and notifications; and a **profile screen** displaying your profile.

For now, there is no server communication and most features aren’t implemented yet — besides most of the **UI**, they mainly showcase the app’s design and potential.

# UI
Mubble was first designed as a wireframe in Figma, went through a lot of iterations, and then the same design was built in Jetpack Compose. The app was designed following Material Design 3 guidelines, and uses the Material Theme Builder for theme generation.

I plan to upload the full Figma document once the design is more finalized — still some things to tweak.
- Supports both **light** and **dark** themes.
- **Dynamic color** (available from Android 12 and above) is planned — not yet implemented. It takes a single color from a user’s wallpaper or in-app content and creates an accessible color scheme applied to UI elements.
- App is also implementing [**Material 3 Expressive**](https://m3.material.io/blog/building-with-m3-expressive), which introduces updated components, shapes, animations, and more expressive UI elements.

For now, the app doesn’t support different screen sizes due to time constraints.

## Roadmap

### Current Status
- Home screen (mostly done; circle item UI may be updated).
- Explore screen (posts / bubbles screen).
- Chat list and chat content screens.
- Profile screen.
- Settings screen.
- Login / Sign up screens.
- Onboarding screen.

### Planned Features
- Different profile screen for current user (partially implemented) and other users.
- Circle screen (similar to profile UI).
- Activity screen (currently empty).
- New post screen.
- Sendable items in chat: images, videos, posts, bubbles, profiles, circles.
- Camera integration for direct posting or sending to friends.
- Dynamic color support.
- Chat bubble custom themes (color changes).
- Shared animated transitions for clickable containers (e.g., posts).
- Image picker inside chat.

### Connecting to Server (Upcoming)
- Firebase integration.
- Real-time chat implementation.
- Fetching posts, bubbles, profiles, and other content online.
- Content caching.

### Long-term Ideas
- Custom feed algorithm.
- Short-form content support (like popular social media platforms).
- Voice messages and phone calls in chat.

### Known Issues / TODOs
- Performance optimization might be needed for some state-driven UI changes in Home, Explore, and Profile screens’ custom scaffold.
- Testing on physical devices is still pending.

## Run Locally

1. Clone the project.

```bash
  git clone https://github.com/aidsyla/mubble.git
```
2. Open the project in Android Studio.
3. Let Gradle sync finish.
4. Run the app on an emulator or physical device (API level 24+).

## Contributing

Suggestions and changes are welcome.  
For major updates, please open an issue first to discuss what you would like to change, or contact me directly.

## License

**Mubble** is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

This project includes and adapts code from the [Now in Android](https://github.com/android/nowinandroid) project, which is also licensed under Apache License 2.0.

Stock images used in this app are from [Unsplash](https://unsplash.com/). While attribution is not required under the [Unsplash License](https://unsplash.com/license), it is appreciated.
