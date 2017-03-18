package com.acecodelabo.singaporesavingsbonds;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.text.Html;

public class MainActivity extends AppCompatActivity {

    String url = "http://www.sgs.gov.sg/savingsbonds/Your-SSB/This-months-bond.aspx";
    ProgressDialog progressDialog;
    private AdView mAdView;
    TextView hyperLink;
    Spanned text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Description().execute();


        hyperLink = (TextView)findViewById(R.id.refLink);

        text = Html.fromHtml("Reference:" +
                "<a href='http://www.sgs.gov.sg/savingsbonds.aspx'>http://www.sgs.gov.sg/savingsbonds.aspx</a>");

        hyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        hyperLink.setText(text);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private class Description extends AsyncTask<Void, Void, Void> {
        String intRate;
        String bondId;
        String period;
        String issueDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Description");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();

                Elements intRateDescr = document.select(".scroll-table tr:nth-child(2) td:nth-child(11) span");
                intRate = intRateDescr.html();

                Elements tables = document.select(".small-constraint table");
                Element issuanceTable = tables.get(0);
                Elements rows = issuanceTable.select("tr");
                //bond id
                Element row = rows.get(0);
                Elements cols = row.select("td");
                bondId = cols.get(0).text();

                //issue date
                Element issueDateRow = rows.get(2);
                Elements issueDateCols = issueDateRow.select("td");
                issueDate = issueDateCols.get(0).text();

                //period
                Element periodRow = rows.get(6);
                Elements periodCols = periodRow.select("td");
                period = periodCols.get(0).text();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView txtDesc = (TextView) findViewById(R.id.rateText);
            txtDesc.setText(intRate + "% p.a.");

            TextView bondIdText = (TextView) findViewById(R.id.bondId);
            bondIdText.setText(bondId);

            TextView issueDateText = (TextView) findViewById(R.id.issueDate);
            issueDateText.setText(issueDate);

            TextView periodText = (TextView) findViewById(R.id.period);
            periodText.setText(period);
            progressDialog.dismiss();
        }
    }
}
