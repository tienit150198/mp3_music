package com.trantri.tdt_music.data.remote;


import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Model.ChuDeAndTheLoai;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.Model.youtube.Youtube;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

// khi tương tác phía server,n~ yêu cầu thì phải gửi lên đúng cấu trúc thì server mới thực hiện
// dùng để gửi lên những phương thức để chúng ta tương tác phía server và sau khi server kết nối đc rồi
// nó sẽ trả dữ liệu về cho thằng này ==> thằng này dùng để gửi phướng thức và dữ liệu từ phía server về.
public interface DataService {
    // key = AIzaSyDCKG-CnrEJ-pDdMXlnp8yizMgIWAcli28
    @GET("search?part=snippet&type=video&maxResults=10&key=AIzaSyDCKG-CnrEJ-pDdMXlnp8yizMgIWAcli28")
    Single<Youtube> queryMvYoutube(@Query("q") String q);

    @GET("songbanner.php")
        // nhận dữ liệu
    Single<List<Quangcao>> getDataBanner();

    @GET("PlaylistSong.php")
    Single<List<Playlist>> getDataPlaylist();

    @GET("chudeandTheLoai.php")
    Single<ChuDeAndTheLoai> getDataChuDeTheLoai();

    @GET("albumSong.php")
    Single<List<Album>> getDataAlbum();

    @GET("BaiHatDuocYeuThich.php")
    Single<List<BaiHatYeuThich>> getDataBaiHatDuocYeuThich();

    @FormUrlEncoded
    @POST("DanhSachBaiHat.php")
        // tương tác và gửi data lên và nhận về
    Observable<List<BaiHatYeuThich>> getDataBaiHatTheoQuangCao(@Field("idquangcao") String idquangcao);

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Observable<List<BaiHatYeuThich>> getDataBaiHatTheoPlaylist(@Field("idplaylist") String idplaylist);

    @GET("DanhSachAllPlaylist.php")
    Single<List<PlaylistAll>> getAllPlaylist();

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Observable<List<BaiHatYeuThich>> getDataBaiHatTheoTheLoai(@Field("idtheloai") String idtheloai);

    @GET("chuDeAll.php")
    Single<List<ChuDe>> getAllChuDe();


    @FormUrlEncoded
        // tương tác và gửi data lên và nhận về
    @POST("TheLoaiTheoChuDe.php")
    Observable<List<TheLoai>> getTheLoaiTheoChuDe(@Field("idchude1") String idchude);

    @GET("AlbumAll.php")
    Single<List<Album>> getAllAlbum();

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Observable<List<BaiHatYeuThich>> getDataBaiHatTheoAlbum(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("UpdateLuotLike.php")
        // tương tác và gửi data lên và nhận về
    Observable<String> getDataLuotLikeBaiHat(@Field("luotthich") String luotlike, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("SearchBH.php")
        // tương tác và gửi data lên và nhận về
    Observable<List<BaiHatYeuThich>> getSearchBaiHat(@Field("keyword") String keyword);
}
