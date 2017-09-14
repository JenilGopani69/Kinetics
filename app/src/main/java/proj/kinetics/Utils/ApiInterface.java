package proj.kinetics.Utils;

import okhttp3.ResponseBody;
import proj.kinetics.Model.Example;
import proj.kinetics.Model.TaskDetails;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by sai on 23/8/17.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> getTaskList(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<Example> getTaskLists(@Field("username") String username, @Field("password") String password);

    @GET("gettaskdetailbyid.php")
    Call<TaskDetails>getTaskDetails(@Query("id") String id);



    @GET("gettaskdetailbyid.php")
    Call<ResponseBody>getTaskDetailsOffline(@Query("id") String id);


    @FormUrlEncoded
    @PUT("updatetasktimer.php")
    Call<ResponseBody> updateTask(
            @Field("userId") String id,
            @Field("taskId") String taskId,
            @Field("duration") String duration,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @PUT("task_status.php")
    Call<ResponseBody> updateTaskStatus(
            @Field("userId") String userId,
            @Field("taskId") String taskId,
            @Field("status") String status);


    @FormUrlEncoded
    @PUT("pause_reason.php")
    Call<ResponseBody> updatePause(
            @Field("userId") String userId,
            @Field("taskId") String taskId,
            @Field("pauseId") String pauseId);

}
