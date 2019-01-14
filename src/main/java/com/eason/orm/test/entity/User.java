package com.eason.orm.test.entity;

import com.eason.orm.annotation.ORMColumn;
import com.eason.orm.annotation.ORMId;
import com.eason.orm.annotation.ORMTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Eason
 * @createTime 2019-01-14 22:19
 * @description
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ORMTable(name = "user")
public class User {

    @ORMId
    @ORMColumn(name = "id")
    private String id;

    @ORMColumn(name = "username")
    private String username;

    @ORMColumn(name = "name")
    private String name;

    @ORMColumn(name = "password")
    private String password;

    @ORMColumn(name = "phone")
    private String phone;

}
