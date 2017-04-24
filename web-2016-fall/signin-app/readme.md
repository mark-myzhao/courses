# Signin
## 测试前请务必follow一下几点
- 请先打开mongodb..请先打开mongodb..
- 确保相应npm包已经安装..若未安装请在signin目录下输入 `npm install` 以安装json中的依赖
- 浏览器: Chrome
- cookies 生存期为15分钟, 超过这个时间会被踢下线的

## 已经完成的部分
- 用户注册页面
- 用户登录页面
- 限制用户访问他人 用户详情
- 用户登出
- 保持登录状态,直到cookies失效或者用户登出
- 加密存储密码
- 进一步完善UI页面

## 备注
- validator.js & userManager.js 参考了王青老师的代码,因为自己实在写不到如此简洁的程度..
- validator.js & userManager.js 为了和自己的程序适应,修改了一些实现,并补充了几个方法
- 数据库在index.js中连接