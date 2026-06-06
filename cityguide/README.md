# CityGuide — мобильное приложение-гид

Android-приложение для просмотра достопримечательностей города с картой, профилем пользователя и избранным.

---

## Функциональность

- **Главный экран** — приветствие и переход к списку достопримечательностей
- **Список достопримечательностей** — просмотр объектов
- **Карта** — отображение достопримечательностей на Google Maps
- **Профиль** — регистрация и вход по номеру телефона, выбор фото из галереи / камеры / встроенных аватаров

---

## Технологии

- Java (Android SDK 34)
- Fragment + BottomNavigationView
- Google Maps SDK
- Retrofit 2 + OkHttp (сетевые запросы)
- Room Database (локальное хранилище)
- Glide (загрузка изображений)
- ViewModel + LiveData

---

## Структура проекта

```
app/src/main/
├── java/com/example/cityguide/
│   ├── MainActivity.java
│   └── ui/
│       ├── home/HomeFragment.java
│       ├── profile/ProfileFragment.java
│       └── attractions/AttractionListFragment.java
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   └── fragment_home.xml
    └── menu/
        └── bottom_nav_menu.xml
```

---

## Как открыть проект

1. Установите [Android Studio](https://developer.android.com/studio)
2. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/Viktoria412/cityguide.git
   ```
3. Откройте папку в Android Studio: **File → Open**
4. Дождитесь синхронизации Gradle
5. В `AndroidManifest.xml` замените `YOUR_GOOGLE_MAPS_API_KEY` на свой ключ из [Google Cloud Console](https://console.cloud.google.com/)
6. Запустите на устройстве или эмуляторе: **Run → Run 'app'**

---

## Минимальные требования

- Android 7.0 (API 24) и выше
- Google Play Services (для карты)
