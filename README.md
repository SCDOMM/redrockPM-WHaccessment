
  红岩移动部门寒假考核项目
## 一.简要介绍
这是一个简简单单的移动项目，集百语言之短，聚古今天下之糟粕，是乃以迪普西克，比利比利，CSDN，稀土撅金，菜鸡编程堆砌之屎山巨作，并由俺寻思之力黏合而成，其寻思，故称其规范屎；bug多，故称其稀烂，自亲濯淖污泥之中，拥抱于浊秽，以贴近尘埃之内，全获世之滋垢，皭然泥而绝滓者也。  
项目名称为“HachiMi”，即韦氏拼音中的“哈基米”。其本来是朝女同学吹牛逼的产物，当时我说“我可以写个app，把学校里的哈基米都存进去”，没想到我还真给这一坨写出来了。
### 1.技术栈
- Android API26和它的动物朋友们
- Java和它的动物朋友们
- 依赖库：
1. gson v2.10.1:用于解析JSON信息的第三方依赖库，用法简单，相当好使
2. OKhttp v5.3.0:用于网络编程的第三方依赖库，用法简单，结构清晰，相当好使
3. security-crypto v1.1.0-alpha06:用于构建EncryptedSharedPreferences的androidx官方依赖库，安全储存Token等信息
4. glide v4.15.1:用于展示，查看图片的第三方依赖库，用法简单，自带异步，相当好使
- 线程通信方式：
1. Handler:用于网络请求情况
2. ViewModel:用于非网络请求情况
- 已有功能
目前为止，通过ViewPager2+fragment+BottomNavigationView，我一共写了三个页面，分别是“基米大全”，“基米动态”和“个人资料”。  
### 2.基米大全和基米动态1
这两个页面通过RecycleView实现了翻页滚动，通过SearchView实现了搜索页面，使用glide同时优化和异步展示图片。同时，通过SwipeRefreshLayout，您可以进行刷新操作。  
- 项目创建了两个集合，一个为原始集合，另一个为搜索集合。
- adapter中使用了glide展示图片，同时实现异步和优化操作。（原本是利用线程池和bitmap回收的，改成直接用glide了）  
- 在“基米大全”中，您可以点击item以查看详细资料。  
- 在“基米动态1”中，您可以点击右下角的FloatingActionButton来跳转到上传页面。
- 在fragment创建的时候，程序会自动网络请求一次档案，并且将结果储存入原始集合和搜索集合中。  
1. 刷新操作的实现方式：  
SRL提供了一个重写方法，在该方法内网络请求一次档案：在Handler中接收到档案后进行判断：若当前处于搜索状态，则请求网络请求一次搜索，若当前处于正常状态，则将搜索集合清空，将结果存入原始集合，随后将原始集合复制到搜索集合。  
2. 搜索操作的实现方式：  
SV提供了两个重写方法，一个监听搜索框的变化，另一个则执行搜索逻辑：若搜索框为空，则恢复主页，取消搜索状态，将原始集合复制到搜索集合；若搜索框不为空，则正常执行搜索逻辑，清空搜索集合，并且网络请求一次搜索。
若没有数据，则展示空；若有数据，则将数据存入搜索集合，随后展示搜索集合。  
此过程中原始集合并未被修改，若你退出搜索，则程序会将原始集合的数据存入搜索集合，随后展示搜索集合。  
3. 查看详细资料的实现方式：  
您可以自己设定一个点击事件，一旦您按下这个item，就会启动一个activity，随后ViewModel会将item中的数据传入这个activity，并让其在activity中显示。
### 3.搜索和基米动态1
由于我写基础框架的时候脑子抽了，导致搜索框和搜索页面不在同一个fragment里，因此需要使用ViewModel来传递搜索的关键词。其他搜索逻辑与上文一致。
### 4.基米动态3和修改资料
基米动态3是实现“动态上传”功能的，个人资料的“修改资料”功能需要上传新头像，因此需要打开相册上传相关的照片至app中。因此涉及到了相册权限。  
我通过ContextCompat来检查Manifest所有的权限，若不具备相关权限，则通过requestPermission弹出相册请求；若具备相关权限，则通过Intent和startActivityForResult打开相册。  
安卓框架自带了两个重写方法，onRequestPermissionsResult用于检测权限请求结果，onActivityResult用于检测相册选择结果。后者可以获得被选择照片的uri，用工具类将其转换为base64编码即可传递。
## 二.项目结构
整个项目分为多层结构：
app-
- repository  
   data  
   model  
   network  
- utils  
- view  
   activity  
   adapter  
   fragment  
### repository层用于进行数据，网络请求相关的操作。
1. data
data层封装了通用的响应数据类，其包含了一个泛型参数/泛型集合，使其可以与Gson配合，解析后端传回的复杂数据
2. model
model层封装了各个fragment或者activity需要用到的数据类，部分用于DTO的数据类使用@SerializedName标记了其json字段，以便于后端反序列化
3. network
network层封装了各个fragment或者activity需要用到的网络请求类，使用OKhttp框架配合Handler进行网络编程，其使用的basicHost和basicPort需要在MainActivity中修改
### utils层是工具层
utils层封装了一批工具类，如以Gson为基础的JsonHandle，用于ESP的SecurityHandle（储存Token），用于将base64、byte[]、bitmap和drawable相互转换的ImageHandle，用于进行线程间通信的ViewModel类等等。
### View层储存fragment，adapter和activity
各种各样的fragment和adapter，activity就储存在里面。
## 三.功能展示
项目后端仓库:https://github.com/SCDOMM/redrockBE-WHaccessment.git
**注:该项目的安卓版本为API26，用其他安卓版本出bug了本人概不负责**
1. 按寻思之力测试法
想要进行该测试，首先您的电脑需要具备MySQL，需要在电脑上下载相应的后端项目，最后在任意IDE（Linux也可以）中运行其中的main.go文件即可。（记得按后端仓库中的教程添加测试数据）
随后，您就可以测试该app的各种功能了。
2. Docker/Linux服务器测试法
我还没学，不会。
3. 使用PostMan进行测试

4. 实际展示
- 登录与退出登录
  顾名思义  
  ![登录与退出登录](https://github.com/user-attachments/assets/6b77bd07-8e51-44ea-9232-73e9c7c2b7e9)
- 查看主页和动态，个人资料，设置
  顾名思义  
  ![查看主页动态设置](https://github.com/user-attachments/assets/74a2fa56-2499-430f-89be-d64737ca052f)
- 注销账号
  顾名思义  
  ![注销账号](https://github.com/user-attachments/assets/2f10f77c-55b5-42d3-a179-18ee30530cc2)
- 注册新账号
  顾名思义  
  ![注册账号](https://github.com/user-attachments/assets/0c2772d8-04d3-4144-96d7-343ad11e7134)
- 以管理员身份登录
  注意：管理员身份需要在后端手动修改  
  ![管理员身份登录](https://github.com/user-attachments/assets/79cbee83-d726-4e76-8491-1c8b6850344a)
- 发布动态
  顾名思义  
  ![发布动态](https://github.com/user-attachments/assets/2e983fb4-0bf6-45cb-827f-755accfcc5af)
- 修改账号资料
  顾名思义  
  ![修改账号资料](https://github.com/user-attachments/assets/222df8f9-1802-48e1-9009-97e0e6adf83a)
- 删除动态(需要管理员身份)
  顾名思义  
  ![删除动态](https://github.com/user-attachments/assets/7221757b-4c27-4e7b-8d8c-fe2009352a2b)
- 上传和删除档案(需要管理员身份)
  顾名思义  
  ![上传和删除档案](https://github.com/user-attachments/assets/7b124d5a-d0e0-47d0-a380-4903c57894ba)
- 动态分类
  **暂不支持**

## 四.心得体会
千万别熬夜，熬夜了第二天压根没有精力敲代码，可能要思考很久甚至问迪普西克才能修一个bug  
光看书是不行的，必须在实践中进行学习，在大风大浪中前进
## 五.有待提升优化的地方
1. 动态的图片无法点开和放大，只能远观而不可亵玩。
2. UI丑的一批，相信后人的智慧
3. 后端没安在Linux服务器/Docker上，每次测试都得连热点
4. network层的代码写的一坨，有条件可以将其封装为一个通用类
5. Handler已经是老东西了，可能需要用其他的东西来代替它以实现线程间通信。
6. “动态分类”功能完全没做，相信后人的智慧 
7. 架构分层意义不明，纯粹是寻思出来的，需要换成更好的架构，如MVVM
8. 杂七杂八的小细节，比如非法字符检验，以及若干可能的小bug
9. 暂不支持暗夜模式（也是UI问题）
