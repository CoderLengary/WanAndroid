# WanAndroid
An Android app for www.wanandroid.com

<div align="center">
	<img src="./art/logo.png" width="128" alt="logo">
</div>

WanAndroid App aims to help people access the latest android articles, which is designed with Material Design style, built on MVP(Model-View-Presenter) architecture with RxJava2, Retrofit2, Realm database, Glide.

The source code in the repository reflects the app which supports mobile devices running Android 6.0+.

###About This Repository And App
This app is inspired by [Espresso](https://github.com/TonnyL/Espresso) which is developed by [TonnyL](https://github.com/TonnyL), [Awesome-WanAndroid](https://github.com/JsonChao/Awesome-WanAndroid) and has a lot of similar elements in design.
And this app is using the WanAndroid API[(doc)](http://www.wanandroid.com/blog/show/2) designed by [HongYang](https://github.com/hongyangAndroid).

### Features

+ Collect many articles of Android.
+ Update the articles everyday.
+ Support collect the articles ,which will synchronize the user data so you can visit [website](http://www.wanandroid.com/lg/collect) to browse the list of articles collected.
+ Support mark the articles so you can read it later. 
+ Day mode and night mode are supported.
+ Support send feedback on using experience from your devices.

### Screenshots



I hope the source code for this app is useful for you as a reference or starting point for creating your own app. Here is some instructions to help you better build and run the code in Android Studio.

Clone the Repository:

```
git clone https://github.com/CoderLengary/WanAndroid
```

Check out the master branch:

```
git checkout master
```

Notice: If you want to review a different branch, replace the `master` with the name you want to checkout (if it does exist). Finally open the `WanAndroid/` directory in Android Studio.

Suggestion: It is better for you to update your Android Studio to version 3.0 when you open this project.

### Libraries Used in This App
Name | Introduction
----- | ------ 
[Android Support Libraries](https://developer.android.com/topic/libraries/support-library/alphabet.html) | The Android Support Library offers a number of features that are not built into the framework. These libraries offer backward-compatible versions of new features, provide useful UI elements that are not included in the framework, and provide a range of utilities that apps can draw on. 
[CircleImageView](https://github.com/hdodenhof/CircleImageView) | A circular ImageView for Android 
[Realm](https://github.com/realm/realm-java) | Realm is a mobile database: a replacement for SQLite & ORMs. 
[Retrofit](https://github.com/square/retrofit) | Type-safe HTTP client for Android and Java by Square, Inc. 
[RxAndroid](https://github.com/ReactiveX/RxAndroid) | RxJava bindings for Android. 
[RxJava](https://github.com/ReactiveX/RxJava) | RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM. 
[Glide](https://github.com/bumptech/glide) | 	An image loading and caching library for Android focused on smooth scrolling.
[AgentWeb](https://github.com/Justson/AgentWeb) | AgentWeb is an Android WebView based, extremely easy to use and powerful library.
[FlowLayout](https://github.com/hongyangAndroid/FlowLayout) | A very convenient and powerful flow layout.
[Banner](https://github.com/youth5201314/banner) | An easy way to add useful banners into android applications.
### Thanks to
+ [TonnyL](https://github.com/TonnyL) Really appreciate his help!
+ [WanAndroid](http://www.wanandroid.com)
+ [Awesome-WanAndroid](https://github.com/JsonChao/Awesome-WanAndroid)
### License
```
Copyright 2018 CoderLengary

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
