# Community
基于地理位置的校园交友社区APP

## 功能介绍：

#### 1.注册登录
注册/登录模块采用json数据上传个人信息，与服务器后台进行数据上传请求操作。当用户已在该设备登录时，将其用户信息采用SharedPreferences保存用户信息。

![主界面](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片1.jpg "主界面")
![注册](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片2.jpg "注册")
![登录](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片3.jpg "登录")

#### 2.个人资料编辑
个人资料展示采用侧边划栏DrawerLayout的效果，侧边菜单可以随着滑动展示与隐藏。个人资料上传时将用户资料信息封装成一个类，利用OkHttp3库将其上传到后台数据库。

![个人资料](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片4.jpg "个人资料")　　
![信息编辑](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片5.jpg "信息编辑")

#### 3.附近用户查询
利用百度MAP的定位功能将用户个人位置信息上传至数据库内，再通过经纬度计算出使用者与其他用户之间的距离，采用雷达扫描的效果将用户以点的形式根据距离分布绘制  出来。用户则利用ViewPager以卡片的形式展示出来，点击用户头像显示其个人资料详情。

![雷达扫描显示](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片6.jpg "雷达扫描显示")　　
![用户资料卡片](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片7.jpg "用户资料卡片")

#### 4.动态发布与展示
动态发布：采用消息+图片的形式来发布，图片限制发送数量最多为9张。动态消息内容将其存储在服务器主机上的文件夹中，并对不同用户的动态进行命名来存放。动态显示：采用Recycleview布局来展示用户动态，单个动态布局为头像+姓名+动态内容+图片+地理位置信息，下部为评论+点赞图标。

![动态发布](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片8.jpg "动态发布")　　
![动态展示](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片9.jpg "动态展示")

#### 5.社区插件功能
将多个插件摆布以转盘的形式布置，转动转盘并点击插件可进入应用查看具体内容。  
主要功能：历史上的今天、今日天气、新闻头条。

![转盘显示](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片10.jpg "转盘显示")
![历史上的今天](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片11.jpg "历史上的今天")
![今日天气](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片12.jpg "今日天气")
![新闻头条](https://github.com/librahfacebook/Community/blob/master/demo/pic/图片13.jpg "新闻头条")

## APP项目演示
<img width=300 height=500 src="https://github.com/librahfacebook/Community/blob/master/demo/demo.gif" alt="App Demo">
