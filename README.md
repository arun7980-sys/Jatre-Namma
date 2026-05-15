# Jatre-Namma Pride 🎪

A digital guide designed for village fairs (Jatre), bringing a vibrant, festive user interface to a modern mobile experience. Jatre-Namma connects attendees with the cultural essence, live events, and practical utilities needed to navigate and enjoy local village fairs to the fullest.

## 🌟 Key Features

- **Live Event Schedule:** Stay updated with a real-time schedule of fair events, performances, and ceremonies.
- **Lost & Found:** A dedicated communication board helping fairgoers report lost items and reunite with found belongings, powered by a real-time feed.
- **Digital Map:** A location-based digital map using OpenStreetMap (`osmdroid`) to help you navigate through the fairgrounds, food stalls, and event zones.
- **Cultural Stories:** Immerse yourself in the rich history and cultural significance of the fair with a dedicated storytelling section.
- **Vibrant UI Theme:** A visually rich and festive UI built with modern Jetpack Compose.

## 🛠 Tech Stack

- **Platform:** Android
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Backend/Database:** Firebase Firestore (NoSQL realtime database) & Firebase Authentication
- **Maps:** `osmdroid` for mapping and location services
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for modern asynchronous image rendering
- **Navigation:** Jetpack Navigation Compose

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- JDK 17
- A Firebase project configured for Android

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/arun7980-sys/Jatre-Namma.git
   ```
2. **Open in Android Studio:**
   Open the cloned directory in Android Studio.
3. **Firebase Configuration:**
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Add an Android app and register it with the package name `com.example.jatrenamma`.
   - Download the `google-services.json` file and place it in the `app/` directory of the project.
   - Enable **Firestore** in the Firebase console.
4. **Build and Run:**
   Sync the Gradle files and run the project on an emulator or physical device.

## 📸 Screenshots

*(Add screenshots of your App's Home, Schedule, Map, and Lost & Found screens here)*

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## 📝 License

This project is open-source and available under the MIT License.
