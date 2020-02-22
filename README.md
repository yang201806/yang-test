# yang-test
面试初试试题

springboot版本2.0.7.RELEASE
beetl版本1.2.14.RELEASE

时间显示格式
yyyy-MM-dd HH:mm:ss

mysql数据库版本5.5.20

支持swagger

打包形式jar

统一返回数据格式
{
	success: false,// true,false
	statusCode:"20000",// 20001
	statusMsg: "",
	data: {
		items:[
			{id: "", name: "", code: ""},
			{id: "", name: "", code: ""}
		],
		total: 2
	}
}

项目结构
controller 控制器
entity 数据表对应实体
service 业务逻辑
dao 操作数据
util 工具类
config 配置类