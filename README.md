# Study MVVM Android

这是一个用于学习和实践现代Android开发的示例项目。该项目基于 **MVVM (Model-View-ViewModel)** 架构模式，并集成了一系列来自 Jetpack 和其他流行第三方库的技术，旨在展示如何构建一个健壮、可维护、可扩展的Android应用。

## ✨ 项目特色

- **100% Kotlin**: 完全使用 Kotlin 语言编写，充分利用其现代、简洁和安全的特性。
- **MVVM 架构**: 遵循 Google 推荐的 MVVM 架构，实现了清晰的职责分离。
- **响应式编程**: 使用 `LiveData` 和 `Kotlin Coroutines` 来处理异步任务和UI状态更新。
- **依赖注入**: 通过 Hilt 实现依赖注入，简化了依赖管理和代码的模块化。
- **现代化UI**: 使用 `ViewBinding` 替代 `findViewById`，并利用 `Material Design` 组件构建美观的界面。
- **强大的导航**: 基于 `Android Jetpack Navigation Component` 构建单Activity的应用架构。
- **本地持久化**: 使用 `Room` 数据库进行本地数据存储。
- **远程数据获取**: 通过 `Retrofit` 和 `OkHttp` 与远程API进行交互。

## 🛠️ 技术栈与组件

本项目使用的核心技术栈和库如下：

*   **核心组件**
    *   [Kotlin](https://kotlinlang.org/): 官方推荐的Android开发语言。
    *   [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): 用于管理后台任务，简化异步编程。
    *   [Android KTX](https://developer.android.com/kotlin/ktx): 优化 Jetpack 和其他 Android API 的 Kotlin 扩展。

*   **架构 (Architecture)**
    *   [MVVM](https://developer.android.com/jetpack/guide): Model - View - ViewModel 架构模式。
    *   [Android Jetpack](https://developer.android.com/jetpack): Google 官方的库集合，用于加速开发。
        *   **ViewModel**: 以注重生命周期的方式管理UI相关的数据。
        *   **LiveData**: 可观察的数据持有者类，具有生命周期感知能力。
        *   **Navigation Component**: 处理应用内的所有导航。
        *   **Room**: 本地数据库，用于对象关系映射 (ORM)。
        *   **DataStore**: 用于存储键值对或类型化对象的数据存储解决方案。

*   **依赖注入 (Dependency Injection)**
    *   [Hilt](https://dagger.dev/hilt/): 基于 Dagger 的依赖注入库，专为Android设计。

*   **网络 (Networking)**
    *   [Retrofit](https://square.github.io/retrofit/): 一个类型安全的HTTP客户端，用于Android和Java。
    *   [OkHttp](https://square.github.io/okhttp/): 高效的HTTP客户端，用作Retrofit的底层。
    *   [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor): 记录HTTP请求和响应信息。
    *   [Gson](https://github.com/google/gson): 用于JSON序列化和反序列化的库。

*   **UI**
    *   [ViewBinding](https://developer.android.com/topic/libraries/view-binding): 以类型安全的方式访问视图。
    *   [Material Components](https://material.io/develop/android/): 谷歌的Material Design组件库。
    *   [SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen): 官方的启动屏API。
    *   [Glide](https://github.com/bumptech/glide): 强大且高效的图片加载和缓存库。
    *   [SwipeRefreshLayout](https://developer.android.com/develop/ui/views/touch-and-input/swipe-to-refresh): 提供下拉刷新的UI模式。

*   **代码生成与处理**
    *   [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html): Kotlin的注解处理器，比KAPT更快。

*   **辅助工具**
    *   [Timber](https://github.com/JakeWharton/timber): 一个强大且可扩展的日志记录工具。
    *   [Kotlin Faker](https://github.com/serpro69/kotlin-faker): 用于生成测试或占位用的假数据。

## 🏛️ 项目架构

本项目遵循经典的MVVM分层架构，确保代码清晰、模块化和可测试。

*   **View (UI Layer)**: 主要由 `Activity` 和 `Fragment` 组成，负责展示UI和响应用户交互。UI层通过 `ViewModel` 观察数据变化来更新自己，并将用户操作通知给 `ViewModel`。
*   **ViewModel (Presentation Layer)**: 作为UI和数据层之间的桥梁。它不持有任何对View的引用，通过 `LiveData` 或 `StateFlow` 向UI层暴露数据。它调用 `Repository` 来获取数据。
*   **Model (Data Layer)**: 由 `Repository`、`DataSource` (本地和远程) 和数据模型类组成。
    *   **Repository**: 统一的数据入口，负责决定是从网络获取数据还是从本地缓存获取。它屏蔽了数据来源的复杂性。
    *   **Remote DataSource**: 使用 `Retrofit` 从远程API获取数据。
    *   **Local DataSource**: 使用 `Room` 数据库进行数据的增删改查和持久化。

```
+----------------+      +------------------+      +------------------+
|      View      |      |    ViewModel     |      |    Repository    |
| (Activity/Frag)|      |                  |      |                  |
|                | <--- | (LiveData/Flow)  | <--- |                  |
| (User Actions) | ---> |                  | ---> |                  |
+----------------+      +------------------+      +------------------+
                                                     /                \
                                                    /                  \
                                        +------------------+    +------------------+
                                        | Remote DataSource|    | Local DataSource |
                                        |    (Retrofit)    |    |      (Room)      |
                                        +------------------+    +------------------+
```

## 🚀 如何运行

1.  **克隆仓库**:
    ```bash
    git clone https://github.com/githubityu/StudyMvvmAndroid.git
    ```
2.  **打开项目**:
    使用最新稳定版的 Android Studio 打开项目。
3.  **同步Gradle**:
    等待 Android Studio 自动下载所有依赖并完成 Gradle 同步。
4.  **运行应用**:
    点击 "Run" 按钮或使用快捷键 `Shift + F10` 来构建并运行应用到一个模拟器或真实设备上。

## 📝 许可证

```
Copyright [2024] [Your Name or Organization]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
