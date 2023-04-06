package com.project.travelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.BookingModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {
    double ADULT_PRICE = 645000, CHILD_PRICE = 322000;
    Map user = new HashMap<String, Object>();
    Map tour = new HashMap<String, Object>();
    EditText bookingDate, edtChild, edtAdult, edName, edPhone;
    Button btnBooking, btnMinusAdult, btnPlusAdult, btnMinusChild, btnPlusChild;
    ImageButton btnGoBack;
    TextView tvTotal;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        user = (Map) getIntent().getSerializableExtra("user");
        tour = (Map) getIntent().getSerializableExtra("tour");

        bookingDate = findViewById(R.id.bookingDate);
        edtAdult = findViewById(R.id.edtAdult);
        edtChild = findViewById(R.id.edtChild);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        btnMinusAdult = findViewById(R.id.btnMinusAdult);
        btnMinusChild = findViewById(R.id.btnMinusChild);
        btnPlusAdult = findViewById(R.id.btnPlusAdult);
        btnPlusChild = findViewById(R.id.btnPlusChild);
        tvTotal = findViewById(R.id.tvTotal);
        btnBooking = findViewById(R.id.btnBooking);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTourTitle = findViewById(R.id.tvTourTitle);
        tvTourTitle.setText(tour.get("TourName").toString());
        setDefaultCustomerInfo(user);
        setDefaultTourInfo(tour);
        dialog = new Dialog(this);
        setDefaultDisableBtn();
        updateTotal();
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingModel booking = createBookingModel();
                TravelBookingDbHelper.saveBooking(new Callback<BookingModel>() {
                    @Override
                    public void callback(BookingModel data) {
                        openOkDialog();
                    }
                }, booking);
            }
        });
        btnMinusAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(edtAdult.getText().toString());
                int nextValue = currentValue - 1;
                edtAdult.setText(Integer.toString(nextValue));
                if (nextValue == 1) {
                    v.setEnabled(false);
                }
                updateTotal();
            }
        });
        btnMinusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(edtChild.getText().toString());
                int nextValue = currentValue - 1;
                edtChild.setText(Integer.toString(nextValue));
                if (nextValue == 0) {
                    v.setEnabled(false);
                }
                updateTotal();
            }
        });
        btnPlusAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(edtAdult.getText().toString());
                int nextValue = currentValue + 1;
                edtAdult.setText(Integer.toString(nextValue));
                if (nextValue > 1) {
                    btnMinusAdult.setEnabled(true);
                }
                updateTotal();
            }
        });
        btnPlusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(edtChild.getText().toString());
                int nextValue = currentValue + 1;
                edtChild.setText(Integer.toString(nextValue));
                if (nextValue > 0) {
                    btnMinusChild.setEnabled(true);
                }
                updateTotal();
            }
        });

        bookingDate.setShowSoftInputOnFocus(false);
        setDefaultBookingDate(bookingDate);
        bookingDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        bookingDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DATE, 1);
                datePickerDialog.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private BookingModel createBookingModel() {
        return new BookingModel(
                UUID.randomUUID().toString(),
                user.get("username").toString(),
                edName.getText().toString(),
                edPhone.getText().toString(),
                Integer.parseInt(tour.get("TourId").toString()),
                tour.get("TourName").toString(),
                bookingDate.getText().toString(),
                Integer.parseInt(edtAdult.getText().toString()),
                ADULT_PRICE,
                Integer.parseInt(edtChild.getText().toString()),
                CHILD_PRICE,
                1,
                tour.get("ImageUrl").toString()
        );
    }

    private void setDefaultTourInfo(Map tour) {
        ADULT_PRICE = Double.parseDouble(tour.get("Price").toString());
        CHILD_PRICE = (double) ADULT_PRICE / 2;

        TextView adultPrice = findViewById(R.id.tvAdultPrice);
        adultPrice.setText(CommonHelper.FormatCurrency(ADULT_PRICE));
        TextView childPrice = findViewById(R.id.tvChildPrice);
        childPrice.setText(CommonHelper.FormatCurrency(CHILD_PRICE));

        TextView tvTourName = findViewById(R.id.tvTourName);
        tvTourName.setText(tour.get("TourName").toString());
    }

    private void setDefaultCustomerInfo(Map user) {
        edName.setText(user.get("name").toString());
        edPhone.setText(user.get("phone").toString());
    }

    private void openOkDialog() {
        dialog.setContentView(R.layout.activity_booking_successfully_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button ok = dialog.findViewById(R.id.okBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(BookingActivity.this, MainActivity.class);
                i.putExtra("loggedUserData", (Serializable) user);
                startActivity(i);
                finish();
            }
        });
        dialog.show();
    }

    private void updateTotal() {
        int adultQuantity = Integer.parseInt(edtAdult.getText().toString());
        int childQuantity = Integer.parseInt(edtChild.getText().toString());

        double total = adultQuantity * ADULT_PRICE + childQuantity * CHILD_PRICE;
        tvTotal.setText(CommonHelper.FormatCurrency(total));
    }

    private void setDefaultDisableBtn() {
        int adultQuantity = Integer.parseInt(edtAdult.getText().toString());
        int childQuantity = Integer.parseInt(edtChild.getText().toString());
        if (adultQuantity == 1) {
            btnMinusAdult.setEnabled(false);
        }
        if (childQuantity == 0) {
            btnMinusChild.setEnabled(false);
        }
    }

    private void setDefaultBookingDate(EditText editText) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        int mYear = tomorrow.get(Calendar.YEAR); // current year
        int mMonth = tomorrow.get(Calendar.MONTH); // current month
        int mDay = tomorrow.get(Calendar.DAY_OF_MONTH); // current day
        editText.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);

    }
}