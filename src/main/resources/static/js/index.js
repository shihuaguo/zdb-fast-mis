function addTab(title, href, icon){
    var tt = $('#tabs');  
    if (tt.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab          
        tt.tabs('select', title);  
        refreshTab({tabTitle:title,url:href});  
    } else {  
    	 var winSize = tt.tabs('tabs');  
    	if(winSize && winSize.length > 15){
    		alert('最多只能打开16个窗口');
    		return;
    	}
    	var height = $(window).height();
    	console.info('+++++height+++++++' + height)
    	height = height-50-35-46 + 'px';
        if (href){  
            var content = '<iframe scrolling="yes" frameborder="0"  src="'+href+'" style="width:100%;height:'+height+';margin-top:10px;margin-left:0px;overflow: hidden;"></iframe>';  
        } else {  
            var content = '未实现';  
        }  
        tt.tabs('add',{  
            title:title,  
            closable:true,  
            content:content
//            ,  
//            iconCls:icon||'icon-default'  
        });  
    }  
}  
/**     
 * 刷新tab 
 * @cfg  
 *example: {tabTitle:'tabTitle',url:'refreshUrl'} 
 *如果tabTitle为空，则默认刷新当前选中的tab 
 *如果url为空，则默认以原来的url进行reload 
 */  
function refreshTab(cfg){  
    var refresh_tab = cfg.tabTitle?$('#tabs').tabs('getTab',cfg.tabTitle):$('#tabs').tabs('getSelected');  
    if(refresh_tab && refresh_tab.find('iframe').length > 0){
    	var _refresh_ifram = refresh_tab.find('iframe')[0];  
    	var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  
    	_refresh_ifram.contentWindow.location.href=refresh_url;  
    }  
}

/**
 * 关闭当前选择的菜单页
 * @returns
 */
function closeSelectTab(){
	 var tab = $('#tabs').tabs('getSelected');//获取当前选中tabs  
	 var index = $('#tabs').tabs('getTabIndex',tab);//获取当前选中tabs的index  
	 $('#tabs').tabs('close',index);//关闭对应index的tabs  
}

/**
 * 关闭对应名字tab页
 * @returns
 */
function closeTab(tabName){
	 $('#tabs').tabs('close',tabName);//关闭对应index的tabs  
}

//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{}},
    //props:{item:{},index:0},
    /*template:[
        '<li :class="{active: (item.type===0 && index === 0)}">',
	        '<a v-if="item.type === 0" href="javascript:;">',
		        '<i v-if="item.icon != null" :class="item.icon"></i>',
		        '<span>{{item.name}}</span>',
		        '<i class="fa fa-angle-left pull-right"></i>',
	        '</a>',
	        '<ul v-if="item.type === 0" class="treeview-menu">',
		        '<menu-item :item="item" :index="index" v-for="(item, index) in item.list"></menu-item>',
	        '</ul>',
	        '<a v-if="item.type === 1" :href="\'#\'+item.url">' +
		        '<i v-if="item.icon != null" :class="item.icon"></i>' +
		        '<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
	        '</a>',
        '</li>'
    ].join('')*/
    template:[
        '<li>',
        '<a v-if="item.type === 0" href="javascript:;">',
        '<i v-if="item.icon != null" :class="item.icon"></i>',
        '<span>{{item.name}}</span>',
        '<i class="fa fa-angle-left pull-right"></i>',
        '</a>',
        '<ul v-if="item.type === 0" class="treeview-menu">',
        '<menu-item :item="item" v-for="item in item.list"></menu-item>',
        '</ul>',
        '<a v-if="item.type === 1" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
        '</li>'
        ].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});
}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);

var vm = new Vue({
	el:'#rrapp',
	data:{
		user:{},
		menuList:{},
		main:"main.html",
		password:'',
		newPassword:'',
        navTitle:"欢迎页"
	},
	methods: {
		getMenuList: function () {
			$.getJSON(baseURL + "sys/menu/nav", function(r){
				vm.menuList = r.menuList;
                window.permissions = r.permissions;
			});
		},
		getUser: function(){
			$.getJSON(baseURL + "sys/user/info", function(r){
				vm.user = r.user;
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改密码",
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: baseURL + "sys/user/password",
					    data: data,
					    dataType: "json",
					    success: function(r){
							if(r.code == 0){
								layer.close(index);
								layer.alert('修改成功', function(){
									location.reload();
								});
							}else{
								layer.alert(r.msg);
							}
						}
					});
	            }
			});
		},
        logout: function () {
            $.ajax({
                type: "POST",
                url: baseURL + "sys/logout",
                dataType: "json",
                success: function(r){
                    //删除本地token
                    localStorage.removeItem("token");
                    //跳转到登录页面
                    location.href = baseURL + 'login.html';
                }
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        }
	},
	created: function(){
		this.getMenuList();
		this.getUser();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});



function routerList(router, menuList){
	//设置左边菜单栏高度，菜单过多时出现滚动条，不用将整个系统页面撑出滚动条
	var height = $(window).height();
	$("#menu_div").height(height-54);
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				/*var url = window.location.hash;
				
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
                $(".sidebar-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();*/
				var url = window.location.hash;
				var menuObj = $("a[href='"+url+"']");
				var menuName = $("a[href='"+url+"']:first").text()
				addTab(menuName, url.replace('#', ''));
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    //设置左边菜单高度
			   
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}
