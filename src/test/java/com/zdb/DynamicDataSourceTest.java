package com.zdb;

import com.zdb.dynamicdatasource.DataSourceContext;
import com.zdb.dynamicdatasource.DynamicDataSource;
import com.zdb.modules.api.entity.UserEntity;
import com.zdb.modules.api.service.UserService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {
    @Autowired
    private UserService userService;

    @Test
    public void test(){
        UserEntity user = userService.queryObject(1L);
        System.out.println(ToStringBuilder.reflectionToString(user));

        //切换数据源
        DynamicDataSource.setDataSource(DataSourceContext.SECOND.getName());
        UserEntity user2 = userService.queryObject(1L);
        System.out.println(ToStringBuilder.reflectionToString(user2));
    }
}
