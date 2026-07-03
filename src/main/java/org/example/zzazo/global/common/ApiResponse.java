package org.example.zzazo.global.common;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess","code","message","data"})
public class ApiResponse <T> {


    @JsonProperty("isSuccess")
    private final boolean success;
    private final String code;
    private final String message;

    private final T data;


    public static <T> ApiResponse<T> success(BaseCode baseCode) {
        return new ApiResponse<>(true, baseCode.getCode(), baseCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(BaseCode baseCode,T data) {
        return new ApiResponse<>(true, baseCode.getCode(), baseCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> failure(BaseCode baseCode) {
        return new ApiResponse<>(false, baseCode.getCode(), baseCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> failure(BaseCode baseCode,T error) {
        return new ApiResponse<>(false, baseCode.getCode(), baseCode.getMessage(), error);
    }


}
