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

    public static void getAllTours(@NonNull Callback<ArrayList<TourModel>> finishedCallback) {
        db.collection("tours").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<TourModel> listOfTours = new ArrayList<TourModel>(queryDocumentSnapshots.toObjects(TourModel.class));
                finishedCallback.callback(listOfTours);
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

    public static  void getPlaceNamesFromPlaceIds(@NonNull Callback<ArrayList<PlaceModel>> finishedCallback, ArrayList<Integer> placeIds){
        db.collection("places").whereIn("placeId", placeIds).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<PlaceModel> listOfPlaces = new ArrayList<PlaceModel>(queryDocumentSnapshots.toObjects(PlaceModel.class));
                finishedCallback.callback(listOfPlaces);
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
                    listOfTours.add(new TourModel(1, 1, "Tour 4 đảo Nam Phú Quốc - 1 ngày", "143 Trần Hưng Đạo, KP 7, TT Dương Đông, H.Phú Quốc, tỉnh Kiên Giang", 9.1F, 645000, 800000, "Tiết kiệm 20%",
                            "https://phuquoctv.vn/assets/uploads/tours/5ee1964a7221b-tour-4-dao-phu-quoc-1-ngay-phuquoctv-4.jpg", "8 giờ 30 phút",
                            Arrays.asList(Arrays.asList("08:30-17:00 Đón khách", "Tham quan đảo Gầm Ghì, lặn ống thở và tắm biển", "Tham quan hòn Bườm hoặc hòn Móng Tay, lặn ống thở và tắm biển", "Tham quan hòn Mây Rút Trong và dùng bữa trưa", "Trải nghiệm dịch vụ đi dưới biển Seawalker (chi phí tự túc)", "Trả khách", "Kết thúc tour.")),
                            "Với cát trắng nguyên sơ và nước màu ngọc lam, khung cảnh của Phú Quốc thực sự là một bữa tiệc cho các giác quan. Bạn sẽ đến với thiên đường Hòn Móng Tay để phơi nắng trên bãi biển xinh đẹp dưới bóng mát của hàng trăm cây cọ. Sau đó, bạn sẽ được khám phá thế giới dưới nước đầy màu sắc khi lặn tại Hòm Gầm Ghì. Sau bữa trưa ngon miệng được chuẩn bị bởi thuỷ thủ đoàn, bạn sẽ được cảm nhận bãi biển cát ngọc ngút ngàn và lặn ống thở trong làn nước trong vắt tại điểm đến cuối cùng, đó chính là Hòn Mây Rút.\n" +
                                    "\n" +
                                    "Chưa hết đâu nhé, vì hãy sẵn sàng cho hoạt động câu cá thú vị tại Hòn Thơm. Chắc chắc bạn sẽ bị mê hoặc bởi không khí biển và không gian cực \"chill\" tại Nam Phú Quốc đấy!"
                    ));
                    listOfTours.add(new TourModel(2, 1, "Tour khám phá 3 đảo tại Phú Quốc - 1 ngày", "Gam Ghi island, Hòn Thơm, Phu Quoc, Kien Giang", 8.7F, 550000, 650000, "Tiết kiệm 15%",
                            "https://static-images.vnncdn.net/files/publish/2022/10/12/du-lich-phu-quoc-thang-10-1338.jpg", "9 giờ",
                            Arrays.asList(Arrays.asList("08:30-17:30 Đón khách", "Tham quan đảo Gầm Ghì, lặn ống thở và tắm biển", "\n" +
                                    "Ăn trưa trên tàu", "Tham quan hòn Mây Rút Trong ", "Trả khách", "Kết thúc tour.")),
                            "Nếu bạn đang muốn có 1 kỳ nghỉ thư giãn hay muốn trải nghiệm các hoạt động biển đa dạng thì hãy chọn ngay Phú Quốc cho kỳ nghỉ tiếp theo của bạn nhé. Phú Quốc thực không hổ danh là hòn đảo thiên đường với bãi cát trắng mịn và làn nước trong xanh màu ngọc bích. Hãy đeo ống thở và lặn ngay xuống biển để khám phá thế giới nhiều màu sắc dưới nước ở Hòn Móng Tay và Hòn Gầm Ghì. Sau đó, nằm thư giãn trên bãi cát mịn dưới bóng hàng dừa xanh mát và ngước mắt ngắm đại dương bao la ở Hòn Mây Rút. Rồi bạn sẽ bịn rịn lắm khi phải nói lời chào đảo ngọc đây.")
                    );
                    listOfTours.add(new TourModel(3, 1, "Đi bộ dưới biển Seawalker và khám phá 4 đảo Phú Quốc - Tour 1 ngày của Namaste", "Nguyễn Văn cừ, tổ 4, kp1, Phú Quốc, Kiên Giang", 9.3F, 1870000, 2200000, "Tiết kiệm 15%",
                            "https://canodulichphuquoc.com/wp-content/uploads/2022/06/seawalker-di-bo-duoi-day-bien-mac-gi-thoai-mai-nhat-800x540.jpg", "9 giờ",
                            Arrays.asList(Arrays.asList("08:30-17:30 Đón khách", "Di chuyển đến Công viên san hô Namaste bằng ca-nô ", "Trải nghiệm du thuyền Nautilus và dịch vụ đi bộ dưới biển", "Tham quan 4 đảo Phú Quốc và di chuyển giữa các đảo bằng ca-nô: hòn Móng Tay, Mây Rút Trong và hòn Gầm Ghì", "Trở về cảng An Thới", "Xe đưa khách về điểm đón ban đầu", "Kết thúc tour.")),
                            "Đã đến lúc bận rộn một tí và khám phá những hòn đảo xinh đẹp và thế giới hải dương lộng lẫy của Phú Quốc. Điểm đến đầu tiên trong ngày của bạn sẽ ở đáy biển! Với Seaworld, giờ đây khám phá đại dương đã trở nên dễ dàng hơn bao giờ hết! Với công nghệ bơm khí vào nón lặn để khách có thể thở trực tiếp như bình thường và với hoạt động được thiết kế không đòi hỏi khả năng biết bơi, bạn chỉ việc đi bộ dưới đáy biển, ngắm và được bao quanh bởi san hô và các loài cá đầy màu sắc, còn lại cứ để Seaworld lo!"));


                    listOfTours.add(new TourModel(4, 2, "Tour đêm du thuyền Emperor Cruises trên vịnh Nha Trang - 5 giờ", "Vĩnh Nguyên, Nha Trang, Khánh Hòa", 9.7F, 985000, 1100000, "",
                            "https://cdn.nhatrangbooking.com.vn/images/uploads/tour-du-thuyen-emperor-cruises-nha-trang-top-2.jpg", "5 giờ",
                            Arrays.asList(Arrays.asList("16:00-17:00 Đón khách tại khách sạn trung tâm thành phố Nha Trang hoặc tại địa điểm theo yêu cầu.", "17:00-19:00 Đến cảng, bạn sẽ được Quản lý trải nghiệm chào đón bằng đồ uống chào mừng", "Giới thiệu về Vịnh Nha Trang, con người địa phương, ẩm thực, văn hóa và những lưu ý về an toàn.", "Bắt đầu du ngoạn qua cảnh quan ngoạn mục của Vịnh Nha Trang lúc mặt trời lặn. ", "19:00-20:30 Khám phá Vịnh Nha Trang về đêm trong khi thưởng thức các loại cocktail và đồ uống khác. Các nghệ sĩ guitar và nghệ sĩ vĩ cầm chơi những bản nhạc du dương trong khung cảnh lãng mạn dưới ánh sao, ánh trăng và ánh đèn thành phố rực rỡ.", "Thưởng thức bữa tối thịnh soạn với âm nhạc và sự phục vụ chu đáo từ đội ngũ nhân viên của Emperor Cruises.", "20:30-20:45 Kết thúc bữa tối lãng mạn và tiếp tục thưởng thức đồ uống tại sảnh khách hoặc trên boong tàu trước khi du thuyền quay trở lại cảng Nha Trang", "Trở về đất liền. Kết thúc chuyến tham quan.")),
                            "Hãy bỏ lại mọi mệt mỏi của cuộc sống sau lưng và bắt đầu chuyến du ngoạn vịnh Nha Trang trên chiếc du thuyền Emperor Cruises. Lấy cảm hứng từ sư đơn giản và tinh tế của Bảo Đại, hoàng đế cuối cùng của chế độ quân chủ trong lịch sử Việt Nam, Emperor Cruises mang lại một trải nghiệm du thuyền sang trọng. Khi chuyến đi bắt đầu, bạn sẽ được ngắm nhìn hoàng hôn buông xuống thật đẹp trên vịnh Nha Trang trong khi nhâm nhi ly cocktail ngon tuyệt. Sau đó, bạn sẽ được thưởng thức bữa tối thịnh soạn cùng các loại rượu ngon dưới tiếng đàn du dương của các nghệ sỹ guitar và violin. Một không gian lãng mạn như thế này thì còn chần chừ gì mà không tận hưởng ngay cùng những người thân yêu của mình nào?"));


                    listOfTours.add(new TourModel(5, 2, "Tour 3 đảo tại Nha Trang bằng tàu siêu tốc - 1 ngày", "172/14/4 Bạch Đằng, Tân Lập, Thành phố Nha Trang, Khánh Hòa 650000", 6.9F, 450000, 500000, "",
                            "https://dulichconvoi.com/wp-content/uploads/2019/04/h%C3%B2n-t%E1%BA%B1m-2.jpg", "7 giờ 30 phút",
                            Arrays.asList(Arrays.asList("08:00-09:00 Đón khách tại trung tâm Nha Trang.", "09:00-11:30 Đến Con Sẻ Tre/ Vịnh San hô", "Khách tự do tắm biển và ngắm nhìn san hô và các loài sinh vật biển trong khi lặn bằng ống thở.","Khách cũng có thể tham gia các hoạt động trên biển như bay dù, mô tô nước, ngắm san hô bằng tàu đáy kinh, lặn bình hơi khám phá đại dương, với chi phí tự túc.", "11:30-13:30 Đến làng chài nổi trên biển. Ăn trưa với thực đơn sau:", "- Tôm sú hấp xả", "- Đà điểu nướng ngũ vị Hoặc Gà chiên mắm",
                                    "- Mực xào chua ngọt", "- Chả cá sốt cà", "- Sò hấp sả ớt", "- Trứng chiên", "- Ràu xào theo mùa", "- Cá chiên xoài bằm", "- Lẩu chua cá mú","- Cơm trắng, trà đá, trái cây", "Khách có thể gọi thêm hải sản tươi sống với chi phí tự túc.", "13:30-15:00 Đến Bãi Sạn hoặc Vịnh San Hô 2, một bãi biển đẹp yên tỉnh thơ mộng giữa biển, một không gian lí tưởng để nghỉ ngơi. Khách có thể tắm biển hoặc nằm tắm nắng thư giãn trên bãi biển với tiếng sóng vỗ rì rào.",
                                    "15:00-15:30 Trả khách tại điểm đón ban đầu. Kết thúc tour.")),
                            "Nếu bạn gặp nhiều nỗi lo âu phiền muộn trong cuộc sống, thì hoà mình vào thiên nhiên sẽ giúp bạn có được những giây phút thư giãn cực kì thoải mái. Không chỉ đắm mình các bãi biển đầy nắng và nước biển trong xanh, bạn còn được tham gia các trò chơi bãi biển cực kì thú vị.\n" +
                            "\n" +
                                    "Nếu bạn tìm kiếm các hoạt động thú vị hơn nữa, thì hãy đội ngay mặt nạ lặn, và tiến ra biển nào! Các rạn san hô tuyệt đẹp và các chú cá đủ màu sắc đang chờ đợi bạn đấy! Nếu bạn nghĩ đó là tất cả những gì bạn sẽ được trải nghiệm trong tour này, thì bạn sai đó nha! Bạn sẽ được thưởng thức một bữa trưa với rất nhiều món hải sản tuyệt ngon, càng ăn sẽ làm bạn càng thêm thèm đấy. Quẳng ngay nỗi lo đi, đi biển nào bạn ơi!"

                    ));

                    listOfTours.add(new TourModel(6, 3, "Tour khám phá Bà Nà Hills - 1 ngày", "Hoà Ninh, Hòa Vang, Đà Nẵng", 9.3F, 722500, 783133, "",
                            "https://tourbana.vn/uploads/11-2019/tour-da-nang-ba-na.jpg", "9 giờ 30 phút",
                            Arrays.asList(Arrays.asList("07:30-08:30 Đón khách và di chuyển đến Bà Nà Hills. ", "08:30-09:00 Đến Bà Nà Hills. ", "09:00-11:30 Di chuyển bằng cáp treo lên Bà Nà Hills và đến cầu Vàng, thăm vườn Le Jardin D'Amour, và chùa Linh Ứng.","11:30-12:00 Từ trạm Debay, tiếp tục đi đến đỉnh núi Chúa.", "12:00-13:00 Dùng bữa trưa buffet kiểu Việt (chỉ áp dụng cho khách hàng mua gói có kèm bữa trưa) ", "13:00-16:00 Di chuyển đến công viên trò chơi Fantasy Park và thăm đền Lĩnh Chúa Linh Từ.", "16:00-16:30 Tạm biệt Bà Nà Hills và di chuyển bằng cáp treo xuống bãi đậu xe.",
                                    "16:30-17:00 Trở về trung tâm Đà Nẵng và trả khách tại điểm đón.")),
                                    "Bà Nà Hills là khu phức hợp giải trí và resort lớn nhất tại Việt Nam. Cùng nhau đi tour và xả láng cả ngày tại Bà Nà Hills ngay nào! Tận hưởng không khí mát lạnh cùng phong cảnh tuyệt vời, ăn hết mình với đủ loại ẩm thực và chơi hết sức với những lễ hội và các hoạt động giải trí đa dạng diễn ra hằng ngày, tất cả đều ngay tại đây!"
                            ));

                    listOfTours.add(new TourModel(7, 3, "Tour premium Bà Nà Hills - 1 ngày", "Bà Nà Hills, Hoà Ninh, Hòa Vang, Đà Nẵng", 9.1F, 1633000, 1638544, "",
                            "https://tourdanangcity.vn/wp-content/uploads/2018/04/cau-vang-ba-na-hills.jpg", "6 giờ 30 phút",
                            Arrays.asList(Arrays.asList("07:30-08:45 Đón khách và di chuyển đến Bà Nà Hills. ", "08:45-09:00 Đến Bà Nà Hills. ", "09:00-11:30 Di chuyển bằng cáp treo lên Bà Nà Hills và đến cầu Vàng, thăm vườn Le Jardin D'Amour, và chùa Linh Ứng.", "11:30-12:00 Từ trạm Debay, tiếp tục đi đến đỉnh núi Chúa.", "12:00-13:00 Dùng bữa trưa buffet kiểu Việt.", "13:00-17:30 Di chuyển đến công viên trò chơi Fantasy Park và thăm đền Lĩnh Chúa Linh Từ.", "17:30-18:00 Tạm biệt Bà Nà Hills và di chuyển bằng cáp treo xuống bãi đậu xe.", "18:00-18:30 Trở về trung tâm Đà Nẵng và trả khách tại điểm đón.")),
                            "Bà Nà Hills là khu phức hợp giải trí và resort lớn nhất tại Việt Nam. Cùng nhau đi tour và xả láng cả ngày tại Bà Nà Hills ngay nào! Tận hưởng không khí mát lạnh cùng phong cảnh tuyệt vời, ăn hết mình với đủ loại ẩm thực và chơi hết sức với những lễ hội và các hoạt động giải trí đa dạng diễn ra hằng ngày, tất cả đều ngay tại đây!"
                    ));

                    listOfTours.add(new TourModel(8, 4, "Tour cắm trại và săn mây tại Đà Lạt - 2N1Đ ", "Quảng trường Lâm Viên, Phường 10, Tp. Đà Lạt, Lâm Đồng", 10.0F, 572000, 715000, "Tiết kiệm 20%",
                            "https://dalatreview.vn/wp-content/uploads/2023/03/z4126655168879_fe6c2575ac9885e8fae71cdd7e204853.jpg", "2 ngày",
                            Arrays.asList(Arrays.asList("Ngày 1 ", "15:30-16:30 Khách gặp hướng dẫn viên tại điểm hẹn nằm tại trung tâm Đà Lạt, sau đó cùng tự di chuyển đến khu cắm trại.", "16:30-19:00 Ngắm hoàng hôn cuối ngày. ", "19:00-00:00 Bên ánh lửa trại cùng nhau thưởng thức BBQ gà nướng, thịt heo nướng, salad trộn, cháo gà, khoai lang nướng, bắp nướng, trà hoặc cà phê.", "Ngày 2", "05:30-07:30 Thức dậy đón bình minh, thưởng thức bữa sáng nhẹ và cà phê ấm nóng. ", "07:30-08:00 Cùng nhau thu dọn lều trại và vệ sinh khu vực xung quanh vì một Đà Lạt Xanh, trải nghiệm xanh. Kết thúc hành trình.")),
                            "Ôi những homies mê mẩn Đà Lạt, những fan trung thành của dã ngoại và những người hệ cắm trại ơi! Lại đến lúc rời khỏi cuộc sống hối hả chỉ trong chốc lát và đắm mình trong thiên nhiên rộng lớn và thanh bình rồi!\n" +"\n" +"Ghé thăm Đồi Đa Phú—một trong những điểm đến được yêu thích nhất đối với những người yêu thích hoạt động ngoài trời , cùng nhau đốt lửa trại ấm áp, ngắm nhìn thung lũng bừng sáng về đêm với những ánh đèn như đom đóm từ đỉnh đồi, và đưa bạn về với những khoảnh khắc êm dịu, nơi không có những lo lắng và trách nhiệm hàng ngày, và là nơi có tự do, an bình và những cuộc phiêu lưu thật mới mẻ."
                    ));
                    listOfTours.add(new TourModel(9, 4, "Tour chèo thuyền SUP & Ăn trưa picnic tại Hồ Tuyền Lâm, Đà Lạt - 1 Ngày", "Phường 4, Dalat, Lâm Đồng 66000", 6.0F, 572000, 715000, "Tiết kiệm 20%",
                            "https://dalatreview.vn/wp-content/uploads/2021/05/271280032_4692058597508698_5649648524554335517_n.jpg", "5 giờ",
                            Arrays.asList(Arrays.asList("09:00-09:30 Gặp hướng dẫn viên tại điểm hẹn và lái đến khu vực tham gia tour. ", "09:30-10:00 Hướng dẫn viên và bạn sẽ bơm hơi cho thuyền SUP và cung cấp cho bạn những dụng cụ cần thiết cho chuyến tham quan như: áo phao (bắt buộc), mái chèo, dây buộc chân.", "10:00-10:30 Hướng dẫn viên sẽ hướng dẫn các kỹ năng chèo SUP và trang bị áo phao, thiết bị an toàn cho bạn cũng như giới thiệu về lịch trình chuyến đi. ", "10:30-12:00 Sau khi mọi thứ đã sẵn sàng, đoàn khởi hành đi thuyền ngược dòng hồ Tuyền Lâm khám phá rừng nguyên sinh. Trên đường đi, đoàn sẽ dừng chân bởi những địa điểm vô cùng đẹp mà thông thường bạn không thấy khi đi xe ngang qua Hồ Tuyền Lâm", "12:00-13:00 Thưởng thức bữa trưa phong cách picnic với bánh mì, chả lụa, rau củ quả tươi và trái cây.", "13:00-14:00 Trở về nơi khởi hành. Kết thúc tour. ")),
                            "Bạn đang tìm kiếm một ngày thật ý nghĩa, thật trọn vẹn về thể chất và cực cực vui khi đang ở Đà Lạt? Chẳng nhìn đâu xa nữa, vì tour này sẽ mang đến cho bạn chính xác điều đó: Một ngày trải nghiệm chèo thuyền SUP tại một nơi không thể lý tưởng hơn—hồ Tuyền Lâm.\n" +"\n" +"Đừng lo lắng nếu bạn là người mới bắt đầu, và cũng đừng vội chán nếu bạn đã là tay chèo thượng hạng. Mục đích của tour chính là đem đến niềm vui cho tất cả mọi người, và cho chúng ta thấy được vẻ đẹp thực sự của hồ, một vẻ đẹp chưa được tìm thấy nếu chúng ta chưa chạm vào mặt nước của bờ hồ Tuyền Lâm yên bình, lộng lẫy."
                    ));

                    listOfTours.add(new TourModel(10, 5, "Khám phá Sapa (Đi xe Dcar Limousine 9 chỗ) - Tour 2N1Đ", "tt. Sa Pa, Sa Pa, Lào Cai", 8.0F, 2409639, 4500000, "",
                            "https://thuexeviphoanggia.com/wp-content/uploads/T%E1%BA%ADn-h%C6%B0%E1%BB%9Fng-v%E1%BA%BB-%C4%91%E1%BA%B9p-4-m%C3%B9a-ch%E1%BB%89-trong-1-ng%C3%A0y-t%E1%BA%A1i-Sa-Pa-691x400.jpg", "2 ngày",
                            Arrays.asList(Arrays.asList("Ngày 1", "07:00-10:00 Xe đón khách tại khách sạn, sau đó khởi hành đi Sa Pa. ", "10:00-13:00 Chiêm ngưỡng khung cảnh núi rừng hùng vĩ trên cung đường cao tốc dài nhất Việt Nam trước khi đến với thị trấn Sa Pa thơ mộng, chìm trong mây.", "Đến thị trấn Sa Pa nơi có rất nhiều dân tộc sinh sống như H’mong, Dao, Tày. ", "Dùng bữa trưa tại nhà hàng. ", "13:00-16:00 Đoàn bắt đầu hành trình khám phá bản làng Má Tra - Tả Phìn hoặc Lao Chải - Tả Van của người H’Mông.", "16:00-20:00 Trở lại thị trấn Sa Pa, dùng bữa tối và nghỉ đêm tại khách sạn.",
                                    "Ngày 2","07:00-09:00 Bữa sáng tại khách sạn. ", "09:00-12:00 Khách chọn 1 trong 2 hành trình sau: ", "1. Bắt đầu hành trình khám phá Hàm Rồng với phong cảnh kỳ vỹ. Thăm vườn lan Đông Dương với đủ loại lan muôn màu sắc, đầu Rồng Thạch Lâm. Vượt qua cổng để đến nơi cao nhất của Hàm Rồng là Sân Mây, nơi giao thoa của đất trời, ngắm nhìn toàn cảnh Sapa trên cao.",
                                    "2. Khởi hành chinh phục đỉnh Fansipan cao 3143m–nóc nhà Đông Dương. Trải nghiệm tuyến cáp treo đạt 2 Kỷ lục Thế giới Guinness World Record. Vé cáp treo tự túc.",
                                    "12:00-15:00 Thưởng thức ẩm thực Tây Bắc với những nguyên liệu tuơi ngon tại nhà hàng thị trấn Sapa.",
                                    "15:00-21:00 Trở về Hà Nội. Kết thúc tour."
                                    )),
                            "Nằm ở Tây Bắc Việt Nam, Sapa là 1 trong những điểm du lịch hấp dẫn cả du khách trong và ngoài nước. Rồi bạn sẽ mê mệt khi đứng trước những thửa ruộng bậc thang xanh mướt ở bản Lao Chải và bản Tả Van. Hai bản này nằm ở thung lũng Mường Hoa xinh đẹp, cách xa những con đường đông khách du lịch ở trung tâm Sapa nên sẽ là địa điểm lý tưởng để bạn hoàn toàn thả hồn với thiên nhiên thanh bình đấy.\n" +"\n"
                                    +"Sau khi khám phá vẻ đẹp tự nhiên của Sapa rồi thì đến ngay với bản Cát Cát, thiên đường của các sản phẩm đồ thủ công độc đáo với nhiều họa tiết sặc sỡ đậm chất miền núi. Chưa hết, bạn còn có thể tìm hiểu về văn hóa của đồng bào dân tộc H'Mông ở bản Cát Cát nữa đấy. Hãy tạm quên đi cuộc sống bận rộn ở thành phố và khám phá Sapa ngay nào."
                    ));
                    listOfTours.add(new TourModel(11, 5, "2D1N Sapa Tour by Bus from Hanoi (Homestay Accommodation)", "Dalat, Lâm Đồng", 8.0F, 1299800, 1749000, "",
                            "https://res.klook.com/images/fl_lossy.progressive,q_65/c_fill,w_1200,h_630/w_80,x_15,y_15,g_south_west,l_Klook_water_br_trans_yhcmh3/activities/vvictnsf8ulbcm8owgxb/Tour%202%20Ng%C3%A0y%201%20%C4%90%C3%AAm%20Tham%20Quan%20Sapa%20B%E1%BA%B1ng%20Xe%20D-Car,%20Kh%E1%BB%9Fi%20H%C3%A0nh%20T%E1%BB%AB%20H%C3%A0%20N%E1%BB%99i.jpg", "2 ngày",
                            Arrays.asList(Arrays.asList("- Khám phá vùng quê xinh đẹp của Việt Nam trong chuyến đi hai ngày đắm chìm tại Sapa!", "- Khám phá Sapa, một thị trấn biên giới và khu chợ chính nằm ở tỉnh Lào Cai yên tĩnh", "- Thưởng thức cảnh quan ngoạn mục của dãy núi Hoàng Liên Sơn, và chứng kiến cuộc sống giản dị của người dân địa phương", "- Tham quan Y Linh Hồ, Tạ Vân và Giang Tà Chải, nơi các thành viên của các dân tộc thiểu số H'Mông, Tày và Dzao đỏ sống", "- Nghỉ đêm tại nhà của gia đình địa phương và khám phá và trải nghiệm văn hóa Việt Nam trực tiếp")),
                            ""
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
