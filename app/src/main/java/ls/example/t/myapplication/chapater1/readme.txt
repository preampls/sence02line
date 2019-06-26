第一章节
1、环境搭建

工程目录
build.gradle，该文件分为项目级与模块级两种，用于描述App工程的编译规则。
proguard-rules.pro，该文件用于描述java文件的代码混淆规则。
gradle.properties，该文件用于配置编译工程的命令行参数，一般无须改动。
settings.gradle，配置哪些模块在一起编译。初始内容为include ':app'，表示只编译app模块。
local.properties，项目的本地配置，一般无须改动。该文件是在工程编译时自动生成的，用于描述开发者本机的环境配置，比如SDK的本地路径、NDK的本地路径等。
示例：
apply plugin: 'com.android.application'
android {
    // 指定编译用的SDK版本号。如28表示使用Android 9.0编译
    compileSdkVersion 28
    // 指定编译工具的版本号。这里的头两位数字必须与compileSdkVersion保持一致，具体的版本号可在sdk安装目录的“sdk\build-tools”下找到
    buildToolsVersion "28.0.3"
    defaultConfig {
        // 指定该模块的应用编号，即App的包名。该参数为自动生成，无需修改
        applicationId "com.example.helloworld"
        // 指定App适合运行的最小SDK版本号。如16表示至少要在Android 4.1上运行
        minSdkVersion 16
        // 指定目标设备的SDK版本号。即该App最希望在哪个版本的Android上运行
        targetSdkVersion 28
        // 指定App的应用版本号
        versionCode 1
        // 指定App的应用版本名称
        versionName "1.0"
    }
    buildTypes {
        release {
            // 指定是否开启代码混淆功能。true表示开启混淆，false表示无需混淆。
            minifyEnabled false
            // 指定代码混淆规则文件的文件名
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    插件
    1. Android Parcelable code generator
    2. Android Code Generator
    3. GsonFormat
    4. Android Postfix Completion
    5. Android Drawable Importer

}
