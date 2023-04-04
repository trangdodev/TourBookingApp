package com.project.travelbooking.helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.project.travelbooking.Callback;
import com.project.travelbooking.model.PlaceModel;
import com.project.travelbooking.model.TourModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelBookingDbHelper {
    public static FirebaseFirestore db = null;

    public static void InitialFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    public static void checkExistUser(@NonNull Callback<Boolean> finishedCallback, String username) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            finishedCallback.callback(false);
                        } else {
                            finishedCallback.callback(true);
                        }
                    }
                });
    }

    public static void createUser(@NonNull Callback<Map<String, Object>> finishedCallback, String name, String phone, String username, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone", phone);
        user.put("username", username);
        user.put("hashedPassword", HashHelper.GeneratePasswordHash(password));
        db.collection("users")
                .add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                finishedCallback.callback(documentSnapshot.getData());
                            }
                        });
                    }
                });
    }

    public static void loginUser(@NonNull Callback<Map<String, Object>> finishedCallback, String username, String password) {
        db.collection("users").whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    finishedCallback.callback(null);
                } else {
                    Map<String, Object> user = queryDocumentSnapshots.getDocuments().get(0).getData();
                    String hashedPassword = user.get("hashedPassword").toString();
                    if (HashHelper.CheckCorrectPassword(hashedPassword, password)) {
                        finishedCallback.callback(user);
                    } else {
                        finishedCallback.callback(null);
                    }
                }
            }
        });
    }

    public static void getAllPlaces(@NonNull Callback<ArrayList<PlaceModel>> finishedCallback) {
        db.collection("places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<PlaceModel> listOfPlaces = new ArrayList<PlaceModel>(queryDocumentSnapshots.toObjects(PlaceModel.class));
                finishedCallback.callback(listOfPlaces);
            }
        });
    }

    public static void countTourOfPlaces(@NonNull Callback<Integer> finishedCallback, int placeId) {
        db.collection("tours").whereEqualTo("placeId", placeId).count().get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                finishedCallback.callback((int) aggregateQuerySnapshot.getCount());
            }
        });
    }

    public static void seedData() {
        seedPlaces();
    }

    public static void seedPlaces() {
        db.collection("places").count().get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long numberOfPlaces = aggregateQuerySnapshot.getCount();
                // If dont have any places we should seed data for them
                if (numberOfPlaces == 0) {
                    ArrayList<PlaceModel> listOfPlaces = new ArrayList<PlaceModel>();
                    listOfPlaces.add(new PlaceModel(1, "Phú Quốc", 5, "https://ngaymoionline-cdn.mastercms.vn/stores/news_dataimages/dangthuthuy/072022/20/16/in_article/3249_hon-thom-phu-quoc.jpg", "Được mệnh danh là hòn đảo lớn nhất Việt Nam, du lịch Phú Quốc may mắn được thiên nhiên ưu đãi với làn nước trong, bờ cát trắng mịn, và những khu rừng hoang sơ. Lỡ lạc chân vào “đảo ngọc” rồi, hãy tận hưởng một chút “Vitamin Sea” trong cái nắng dịu nhẹ ở Bãi Dài phía tây Phú Quốc và Bãi Sao phía đông Phú Quốc. Du lịch Phú Quốc không chỉ có biển, hãy thử các trải nghiệm thú vị khác như khám phá hòn đảo bằng xe máy, tham quan công viên bảo tồn động vật Vinpearl Safari, lặn san hô, hay chèo thuyền kayak. Cuối cùng, đừng quên thưởng thức các món hải sản ngon tuyệt vời khi du lịch Phú Quốc tự túc nhé!"));
                    listOfPlaces.add(new PlaceModel(2, "Nha Trang", 4, "https://bcp.cdnchinhphu.vn/344443456812359680/2022/12/27/nhattrang3-16721128389061596602579.jpg", "Trái với một chút se lạnh của núi rừng Đà Lạt mộng mơ là một thành phố biển Nha Trang cực kì năng động trong mắt du khách. Chỉ cần dạo quanh trên con đường Trần Phú khi du lịch Nha Trang tự túc, chắc hẳn bạn sẽ bị ấn tượng bởi bãi biển xanh cát trắng dài vô tận, được điểm xuyết bởi nét hiện đại của những khách sạn 5 sao nổi tiếng. Ngoài ra du lịch Nha Trang còn nổi tiếng với vẻ đẹp hoang sơ của hàng chục đảo lớn nhỏ, công viên giải trí Vinpearl Land, nhà thờ đá cổ kính, hay kho tàng lịch sử văn hóa tại Tháp Bà Po Nagar. Và đừng quên càn quét thiên đường ẩm thực với vô vàn món ngon như bánh căn, bánh xèo, bún cá sứa hay thiên đường hải sản khi du lịch Nha Trang tự túc nhé!"));
                    listOfPlaces.add(new PlaceModel(3, "Đà Nẵng", 5, "https://file1.dangcongsan.vn/data/0/images/2021/03/22/ngocnhcd/kyquan14875978-089-809-90-9.jpg", "Với tốc độ phát triển nhanh như vũ bão, quả không ngoa khi Đà Nẵng được mệnh danh là thành phố đáng sống nhất Việt Nam. Từ những cây cầu hùng vĩ, những bãi biển trong xanh đến vẻ đẹp trời ban của bán đảo Sơn Trà đã khiến du lịch Đà Nẵng tự túc trở nên tuyệt vời hơn bao giờ hết. Không dừng lại ở đó, du lịch Đà Nẵng tự túc còn hấp dẫn du khách với ẩm thực đa dạng, các hoạt động về đêm thú vị và con người thân thiện. Cuối cùng hãy ghé qua những địa điểm gần Đà Nẵng là di sản văn hoá thế giới UNESCO như Hoàng thành Huế, phố cổ Hội An, và thánh địa Mỹ Sơn để chuyến du lịch Đà Nẵng trong mơ của bạn trở nên thật trọn vẹn nhé!"));
                    listOfPlaces.add(new PlaceModel(4, "Đà Lạt", 4, "https://www.dalattrip.com/dulich/media/2017/12/thanh-pho-da-lat.jpg", "Bạn có biết tại sao du lịch Đà Lạt chưa bao giờ hết hot không? Vì Đà Lạt may mắn được thiên nhiên ưu đãi với vị trí nằm ẩn mình trên vùng đất Tây Nguyên đại ngàn và khí hậu ôn hòa quanh năm. Ngoài ra thành phố được mệnh danh là Paris thu nhỏ này luôn thu hút hàng ngàn du khách trong và ngoài nước với những cánh rừng thông xanh quanh năm hay những khu vườn dâu, cà phê, và hoa luôn vươn mình trong nắng. Và đã du lịch Đà Lạt tự túc rồi thì đừng bỏ qua những điểm du lịch nổi tiếng tại đây như núi Lang Biang, nhà ga Đà Lạt, hồ Xuân Hương, và nhà thờ Domaine De Marie. Và cuối cùng hãy để chuyến du lịch Đà Lạt tự túc của bạn thật trọn vẹn với các món đặc sản như bánh tráng nướng, kem bơ, và cà phê."));
                    listOfPlaces.add(new PlaceModel(5, "Sapa", 3, "https://vnn-imgs-f.vgcloud.vn/2020/05/21/17/bi-kip-du-lich-sapa-ngon-bo-re-he-nay.jpg", "Bỏ lại chốn thành thị khói bụi để tìm về chút bình yên tại thị trấn Sapa, một thị trấn nhỏ của vùng núi Tây Bắc. Nổi tiếng với những thửa ruộng bậc thang vàng óng, các bản làng núp mình trên sườn núi cao, nền văn hóa và ẩm thực đa dạng của các dân tộc thiểu số, vì thế chẳng quá khó hiểu khi du lịch Sapa luôn được lòng du khách trong và ngoài nước. Và để có thể ngắm nhìn rõ nét vẻ đẹp của các dãy núi hùng vĩ và bầu trời mây bềnh bồng khi du lịch tự túc Sapa, trekking chính là sự lựa chọn tuyệt vời của bạn. Đừng chỉ ngắm nhìn Sapa trong tranh ảnh và những thước phim nữa, hãy mạnh dạn du lịch Sapa ngay để được một lần hòa mình trong cảnh sắc quyến rũ nơi đây."));
                    WriteBatch batch = db.batch();
                    listOfPlaces.forEach(place -> {
                        DocumentReference newPlaceRef = db.collection("places").document();
                        batch.set(newPlaceRef, place.toMap());
                    });
                    batch.commit();
                }
            }
        });
    }

    public static void seedTours() {
        db.collection("tours").count().get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long numberOfTours = aggregateQuerySnapshot.getCount();
                // If dont have any places we should seed data for them
                if (numberOfTours == 0) {
                    ArrayList<TourModel> listOfTours = new ArrayList<TourModel>();
                    listOfTours.add(new TourModel(1, 1, "Tour 4 đảo Nam Phú Quốc - 1 ngày", "143 Trần Hưng Đạo, KP 7, TT Dương Đông, H.Phú Quốc, tỉnh Kiên Giang", 9.1, 645000, 800000, "Tiết kiệm 20%",
                            "https://phuquoctv.vn/assets/uploads/tours/5ee1964a7221b-tour-4-dao-phu-quoc-1-ngay-phuquoctv-4.jpg", "8 giờ 30 phút",
                            Arrays.asList(Arrays.asList("08:30-17:00 Đón khách", "Tham quan đảo Gầm Ghì, lặn ống thở và tắm biển", "Tham quan hòn Bườm hoặc hòn Móng Tay, lặn ống thở và tắm biển", "Tham quan hòn Mây Rút Trong và dùng bữa trưa", "Trải nghiệm dịch vụ đi dưới biển Seawalker (chi phí tự túc)", "Trả khách", "Kết thúc tour.")),
                            "Với cát trắng nguyên sơ và nước màu ngọc lam, khung cảnh của Phú Quốc thực sự là một bữa tiệc cho các giác quan. Bạn sẽ đến với thiên đường Hòn Móng Tay để phơi nắng trên bãi biển xinh đẹp dưới bóng mát của hàng trăm cây cọ. Sau đó, bạn sẽ được khám phá thế giới dưới nước đầy màu sắc khi lặn tại Hòm Gầm Ghì. Sau bữa trưa ngon miệng được chuẩn bị bởi thuỷ thủ đoàn, bạn sẽ được cảm nhận bãi biển cát ngọc ngút ngàn và lặn ống thở trong làn nước trong vắt tại điểm đến cuối cùng, đó chính là Hòn Mây Rút.\n" +
                                    "\n" +
                                    "Chưa hết đâu nhé, vì hãy sẵn sàng cho hoạt động câu cá thú vị tại Hòn Thơm. Chắc chắc bạn sẽ bị mê hoặc bởi không khí biển và không gian cực \"chill\" tại Nam Phú Quốc đấy!"
                    ));
                    WriteBatch batch = db.batch();
                    listOfTours.forEach(tour -> {
                        DocumentReference newTourRef = db.collection("tours").document();
                        batch.set(newTourRef, tour.toMap());
                    });
                    batch.commit();
                }
            }
        });
    }
}
