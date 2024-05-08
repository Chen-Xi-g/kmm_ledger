# Kotlin Multiplatform 忞鹿记账

使用 Kotlin Multiplatform 开发的跨平台记账应用，支持 Android、iOS、Desktop 平台的深色模式、横竖屏、宽窄屏。
由于Web平台目前处于实验阶段，所以暂时不支持Web平台。待Web平台稳定后会考虑支持。

关于 Kotlin Multiplatform 的介绍可以从官网了解：[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)

## 分支说明

- `main` 分支为主分支，包含了忞鹿记账所有平台的代码。
- `empty_project` 分支为空项目分支，可以从这个分支开始自己的项目，开箱即用。
- `plugin_template` 分支为插件模板分支，可以从这个分支新增自己的插件功能，该插件可以配合`empty_project`一键创建Screen/ViewModel/Model代码。

## 项目说明

这个项目作为个人学习研究 Kotlin Multiplatform 的 Demo，目前已经实现了基本的记账功能，后续会考虑开源设计图与后端代码.

由于本人是 Android 开发者，所以对于`设计图/后端代码`不是很擅长，如果有小伙伴有兴趣可以一起开发，欢迎联系我。

设计图使用Figma, 后端使用RuoYi搭建。

## 体验地址

管理后台：[忞鹿记账后台](http://ht.ledger.plf.minlukj.com/)。由于是自己的项目，不熟悉后台的东西，所以不提供账号密码了。

接口地址：http://ht.ledger.api.minlukj.com/

**移动端账号：**abcd1234
**移动端密码：**1234qwer

## 应用截图

**首页**
![首页](/images/home.png)

**新增账单**
![新增账单](/images/add.png)

**我的**
![我的](/images/mine.png)