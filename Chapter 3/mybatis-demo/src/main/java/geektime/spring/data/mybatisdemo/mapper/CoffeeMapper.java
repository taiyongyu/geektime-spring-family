package geektime.spring.data.mybatisdemo.mapper;

import geektime.spring.data.mybatisdemo.model.Coffee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CoffeeMapper {
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true) // 使用自动生成主键
    Long save(Coffee coffee);

    @Select("select * from t_coffee where id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果，做一个由下划线到驼峰式的转换。
            // @Result(column = "update_time", property = "updateTime"),

            /**
             * mybatis默认是属性名和数据库字段名一一对应的，即
             * 数据库表列：user_name
             * 实体类属性：user_name
             * 但是java中一般使用驼峰命名
             * 数据库表列：user_name
             * 实体类属性：userName
             *
             * 在Springboot中，可以通过设置map-underscore-to-camel-case属性为true来开启驼峰功能，把数据库
             */
    })
    Coffee findById(@Param("id") Long id);
}
