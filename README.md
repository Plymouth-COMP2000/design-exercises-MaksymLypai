# DineEasy - Restaurant Reservation System

A modern Android application for restaurant table reservations and menu management, developed as part of COMP2000 Assessment 2 at the University of Plymouth.

## Overview

DineEasy is a comprehensive restaurant management application that serves two distinct user roles:
- **Guests**: Browse the menu, search for items, create and manage reservations, and receive notifications
- **Staff**: Full CRUD operations for menu items and reservations, with the ability to update reservation statuses

## Features

### Guest Features
- **Menu Browsing**: View all available menu items with descriptions, prices, and categories
- **Search Functionality**: Quick search through menu items by name or description
- **Reservation Management**: Create, view, edit, and cancel table reservations
- **Notifications**: Receive updates about reservation confirmations, status changes, and menu updates
- **Notification History**: View past notifications in the account section
- **Notification Preferences**: Customize notification settings for reservations, menu updates, and special offers

### Staff Features
- **Menu Management**: Full CRUD operations (Create, Read, Update, Delete) for menu items
- **Reservation Management**: View all reservations, update statuses (Pending, Confirmed, Cancelled), and delete reservations
- **Status Updates**: Change reservation statuses with automatic guest notifications
- **All Guest Features**: Staff members have access to all guest functionality

### Additional Features
- **Role-Based Access Control**: Different UI elements and permissions based on user role
- **Material Design UI**: Modern, intuitive interface with bottom navigation
- **Local Data Persistence**: Room database for offline functionality
- **User Authentication**: Secure login system with API integration
- **Session Management**: Persistent login sessions with SharedPreferences

## Technical Stack

### Development Environment
- **IDE**: Android Studio Otter 2025.2.2 Patch 1
- **Language**: Java
- **Minimum SDK**: API Level 24 (Android 7.0)
- **Target SDK**: API Level 34 (Android 14)

### Libraries & Frameworks
- **Room Database** (2.6.1): SQLite object mapping for local data persistence
- **Retrofit** (2.9.0): REST API client for user authentication
- **Material Design Components**: UI components including CardView, RecyclerView, BottomNavigationView, FloatingActionButton
- **Gson** (2.10.1): JSON serialization/deserialization

### Architecture & Patterns
- **Repository Pattern**: Centralized data access layer
- **Singleton Pattern**: Database instance management
- **ExecutorService**: Background thread operations for database queries
- **MVVM-inspired**: Separation of concerns with activities, repositories, and database layers

## Project Structure

```
DineEasy/
├── app/src/main/java/com/example/dineeasy/
│   ├── MainActivity.java                 # Login screen
│   ├── HubActivity.java                  # Dashboard after login
│   ├── MenuActivity.java                 # Menu browsing with search
│   ├── MenuItemDetailsActivity.java      # Add/Edit menu items (staff)
│   ├── ReservationsActivity.java         # View and manage reservations
│   ├── AddReservationActivity.java       # Create/Edit reservations
│   ├── AccountActivity.java              # User profile and notification settings
│   ├── adapters/
│   │   ├── MenuAdapter.java              # RecyclerView adapter for menu items
│   │   ├── ReservationAdapter.java       # RecyclerView adapter for reservations
│   │   └── NotificationAdapter.java      # RecyclerView adapter for notifications
│   ├── database/
│   │   ├── AppDatabase.java              # Room database configuration
│   │   ├── dao/
│   │   │   ├── MenuItemDao.java          # Menu item data access
│   │   │   ├── ReservationDao.java       # Reservation data access
│   │   │   └── NotificationDao.java      # Notification data access
│   │   └── entities/
│   │       ├── MenuItem.java             # Menu item entity
│   │       ├── Reservation.java          # Reservation entity
│   │       └── NotificationEntity.java   # Notification entity
│   ├── repository/
│   │   ├── MenuRepository.java           # Menu data repository
│   │   └── ReservationRepository.java    # Reservation data repository
│   ├── api/
│   │   ├── ApiService.java               # Retrofit API interface
│   │   └── RetrofitClient.java           # Retrofit client configuration
│   └── utils/
│       ├── SessionManager.java           # User session and preferences
│       └── NotificationHelper.java       # Android notification management
├── app/src/main/res/
│   ├── layout/                           # XML layout files
│   ├── menu/                             # Bottom navigation menu
│   └── values/                           # Colors, strings, themes
└── app/build.gradle                      # App-level Gradle configuration
```

## Database Schema

### MenuItem Table
- `id` (Primary Key, Auto-generated)
- `name` (String)
- `description` (String)
- `price` (Double)
- `category` (String)
- `imageUrl` (String)

### Reservation Table
- `id` (Primary Key, Auto-generated)
- `username` (String)
- `date` (String)
- `time` (String)
- `numberOfPeople` (Integer)
- `status` (String: "Pending", "Confirmed", "Cancelled")

### Notification Table
- `id` (Primary Key, Auto-generated)
- `username` (String)
- `title` (String)
- `message` (String)
- `timestamp` (Long)
- `isRead` (Boolean)

## User Roles

The application supports two user roles determined by the authentication API:

### Guest Users
- Username: Any valid username
- Role: `isStaff = false`
- Access: Menu viewing, search, reservation CRUD, notifications

### Staff Users
- Username: Staff member credentials
- Role: `isStaff = true`
- Access: All guest features + menu CRUD + reservation status management

## Installation & Setup

1. Clone the repository:
```bash
git clone https://github.com/Plymouth-COMP2000/design-exercises-MaksymLypai.git
```

2. Open the project in Android Studio Otter 2025.2.2 or later

3. Sync Gradle dependencies

4. Configure the API endpoint in `RetrofitClient.java` if needed

5. Run the application on an emulator or physical device (Android 7.0+)

## Usage

### First Launch
1. Launch the application
2. Login with valid credentials
3. The app will automatically detect if you're a guest or staff member
4. Navigate using the bottom navigation bar

### Creating a Reservation (Guest/Staff)
1. Navigate to "Reservations" tab
2. Click the floating action button (+)
3. Fill in date, time, and number of people
4. Click "Save"
5. Reservation will be created with "Pending" status

### Managing Menu Items (Staff Only)
1. Navigate to "Menu" tab
2. Click the floating action button (+) to add a new item
3. Click "Edit" on any menu item card to modify it
4. Click "Delete" to remove an item (with confirmation)

### Managing Reservations (Staff Only)
1. Navigate to "Reservations" tab
2. View all reservations from all users
3. Click "Edit" to change reservation details
4. Use the dropdown to change status (Pending/Confirmed/Cancelled)
5. Guests receive automatic notifications of status changes

### Notification Settings
1. Navigate to "Account" tab
2. Toggle switches for:
   - Reservation Updates
   - Special Offers and Promotions
   - Menu Updates
3. View recent notifications in the notification history section

## Development Milestones

- **Milestone 1**: Navigation system with basic UI and authentication
- **Milestone 2C**: Room database implementation with API integration
- **Milestone 2D**: Menu CRUD with RecyclerView
- **Milestone 2E**: Reservation CRUD with RecyclerView
- **Milestone 3**: Notification system with user preferences and history
- **Final**: Complete menu item add/edit functionality

## Known Limitations

- Image URLs for menu items are stored but not displayed (placeholder for future enhancement)
- Offline mode uses local database; requires internet connection for authentication
- Date and time pickers use manual text input
- No image upload functionality (URLs only)

## Future Enhancements

- Image display for menu items
- Calendar and time picker dialogs
- Push notifications for real-time updates
- Payment integration
- Table availability checking
- Multi-language support
- Dark mode theme

## Version Control

This project uses Git for version control and is hosted on GitHub. The repository follows a linear commit history with descriptive commit messages.

## Academic Context

**Course**: COMP2000 - Software Engineering
**Institution**: University of Plymouth
**Student ID**: 10868210
**Assessment**: Assessment 2 - Android Application Development

## License

This project is developed for academic purposes as part of university coursework.

## Contact

For questions or feedback regarding this project, please contact through the University of Plymouth student portal.
