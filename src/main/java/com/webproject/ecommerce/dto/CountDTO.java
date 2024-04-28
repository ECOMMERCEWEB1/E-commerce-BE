package com.webproject.ecommerce.dto;

public class CountDTO {
Long count;
public Long getCount(){
    return count;
}
private void setCount(Long count){
    this.count = count;
}
public CountDTO(Long count){
    this.count = count;
}
}
