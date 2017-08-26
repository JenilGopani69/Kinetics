package proj.kinetics.Utils;

import okhttp3.ResponseBody;
import proj.kinetics.Model.Dependenttask;
import proj.kinetics.Model.Example;
import proj.kinetics.Model.Task;
import proj.kinetics.Model.TaskDetails;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sai on 23/8/17.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("gettaskdetail.php")
    Call<ResponseBody> getTaskList(@Field("username") String username);

    @FormUrlEncoded
    @POST("gettaskdetail.php")
    Call<Example> getTaskLists(@Field("username") String username);

    @GET("gettaskdetailbyid.php")
    Call<TaskDetails>getTaskDetails(@Query("id") String id);


}
