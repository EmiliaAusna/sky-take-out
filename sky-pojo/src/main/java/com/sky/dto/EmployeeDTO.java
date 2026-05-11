package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "新增员工请求参数")
public class EmployeeDTO implements Serializable {

    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
