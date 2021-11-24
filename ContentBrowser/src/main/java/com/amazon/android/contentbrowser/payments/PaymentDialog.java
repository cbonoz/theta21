package com.amazon.android.contentbrowser.payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.android.contentbrowser.R;
import com.amazon.android.model.content.Content;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.amazon.android.contentbrowser.payments.PayIdHelper.HTTP_CLIENT;

public class PaymentDialog {

    private static final String MASTER_THETA_ADDRESS = "0x9F1E8c0DdCFB1c97368F409b338Add1BF3030Ea6";
    private static final int QR_SIZE = 200;

    public static void createPaymentDialog(Activity context,
                                           Content content,
                                           DialogInterface.OnClickListener onClickListener)
            throws Exception {

        ViewGroup subView = (ViewGroup) context.getLayoutInflater().// inflater view
                inflate(R.layout.pay_id_input_dialog, null, false);

        TextView purchaseText = subView.findViewById(R.id.pay_id_text);
        purchaseText.setText(String.format(Locale.US, "Lending to: %s", content.getTitle()));

        TextView conversionText = subView.findViewById(R.id.conversion_text);
        final String text = "Scan the QR code below to initiate the loan from a compatible mobile wallet. Your sender/wallet address will automatically be recorded as the lending address.\n\nPay in either Theta or TFuel";
        conversionText.setText(text);

        final Picasso picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(HTTP_CLIENT)).build();
        picasso.setLoggingEnabled(true);

        new Handler(Looper.getMainLooper()).post(() -> {
            String sizeString = QR_SIZE + "%" + QR_SIZE;
            final String url = String.format(Locale.US, "https://api.qrserver.com/v1/create-qr-code/?size=%s&data=%s", sizeString, MASTER_THETA_ADDRESS);
            ImageView v = subView.findViewById(R.id.paymentImage);
            picasso.load(url).into(v);

            new AlertDialog.Builder(context)
                    .setView(subView)
                    .setTitle("Scan address to complete loan")
                    .setPositiveButton("Done", onClickListener)
//                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .show();

        });


    }
}
