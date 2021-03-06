趣事分享App不仅涉及到了图片的内存优化和缓存问题，同时还涉及到自定义控件，结合使用第三方框架来提高效率。日常趣事分享App是基于Android平台，主要包含最新趣事模块、最热趣事模板、趣事推荐模板、图片上传模块以及用户记录等模块。
登录模块由LoginActivity构成，采用Material Design库中的TextInputLayout控件设计账号和密码框，使用ButterKnife库注解功能绑定账号密码控件以及登录按钮点击事件。
注册模块由SigninActivity构成，同样采用Material Design库中的TextInputLayout控件设计账号和密码框以及昵称框，使用ButterKnife库注解功能绑定账号密码控件以及注册按钮点击事件。
趣事模块由LatestFragment构成，其主要展示的是用户进入App后最新发布的趣事内容。
最热模块由HotFragment构成，展示的是最近几天点赞数和评论数最多的趣事，通过点赞数进行降序排序，将当前热度最高的趣事展示在用户的眼前。
推荐模块由RecommendFragment构成，用于根据用户的口味推荐趣事。主要原理是根据用户选择的喜爱趣事类型进行查询，当用户未设置喜爱趣事类型时系统随机进行推荐
个人中心模块由MyFragment构成，其内容是用户发布过的趣事记录，用户可对其进行管理。个人中心模块使用到了AndroidSwipeLayout框架进行帖子删除。滑动帖子即可对其进行删除操作，当帖子成功删除后会出现提示对话框。
设置模块包括用户头像上传，账号注销以及个人相关信息管理等操作，在主活动MainActivity中，能够进行账号注销以及头像管理操作
评论与点赞模块用于用户与其他用户之间进行交流互动，用户可在趣事下方发表自己对某一趣事的看法，用户可以为某一趣事进行点赞，为了保证点赞功能模块正常运行，同一用户每日只能为某一趣事点赞一次。
为了提高系统开发效率，同时使得系统后端更加稳定，系统后端使用Bmob后端云提供的云数据库进行开发，_User表由Bmob后端云提供，其它数据库表根据需求自行创建。app部分截图如下：



![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E6%8E%A8%E8%8D%90%E6%A8%A1%E5%9D%971.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E6%8E%A8%E8%8D%90%E6%A8%A1%E5%9D%972.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E6%9C%80%E6%96%B0%E6%A8%A1%E5%9D%971.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E6%9C%80%E6%96%B0%E6%A8%A1%E5%9D%972.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E6%9C%80%E7%83%AD%E6%A8%A1%E5%9D%971.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E8%AE%BE%E7%BD%AE%E6%A8%A1%E5%9D%972.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E8%AF%84%E8%AE%BA%E7%82%B9%E8%B5%9E%E6%A8%A1%E5%9D%972.png)
![image](https://github.com/Yinta/QuXiang/blob/master/screenshot/%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83%E6%A8%A1%E5%9D%97.png)
