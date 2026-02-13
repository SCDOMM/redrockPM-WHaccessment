# redrockPM-WHaccessment
  红岩移动部门寒假考核项目
## 一.简要介绍
这是一个简简单单的移动项目，集百语言之短，聚古今天下之糟粕，是乃以迪普西克，比利比利，CSDN，稀土撅金，菜鸡编程堆砌之屎山巨作，并由俺寻思之力黏合而成，其寻思，故称其规范屎；bug多，故称其稀烂，自亲濯淖污泥之中，拥抱于浊秽，以贴近尘埃之内，全获世之滋垢，皭然泥而绝滓者也。  
项目名称为“HachiMi”，即韦氏拼音中的“哈基米”。其本来是朝女同学吹牛逼的产物，当时我说“我可以写个app，把学校里的哈基米都存进去”，没想到我还真给这一坨写出来了。  
- 当前所用的依赖库：gson，OKhttp，security-crypto（用于ESP），glide  
- 当前所用的线程通信方式：Handler，ViewModel  
- 目前为止，通过ViewPager2+fragment+BottomNavigationView，我一共写了三个模块，分别是“基米大全”，“基米动态”和“个人资料”。  
### 1.基米大全和基米动态1
这两个模块通过RecycleView实现了翻页滚动，通过SearchView实现了搜索页面，使用glide同时优化和异步展示图片。同时，通过SwipeRefreshLayout，您可以进行刷新操作。  
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
### 2.搜索和基米动态1
由于我写基础框架的时候脑子抽了，导致搜索框和搜索页面不在同一个fragment里，因此需要使用ViewModel来传递搜索的关键词。其他搜索逻辑与上文一致。
### 3.基米动态3和修改资料
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
困死了，明天再做。
## 四.心得体会
千万别熬夜  
光看书是不行的，必须在实践中进行学习，在大风大浪中前进
## 五.有待提升优化的地方
1. 动态的图片无法点开和放大，只能远观而不可亵玩。
2. UI丑的一批，交给后人的智慧
3. 后端没安在Linux服务器/Docker上，每次测试都得连热点
4. network层的代码写的一坨，有条件可以将其封装为一个通用类
5. Handler已经是老东西了，可能需要用其他的东西来代替它以实现线程间通信。
6. “动态分类”功能完全没做，交给后人的智慧 
