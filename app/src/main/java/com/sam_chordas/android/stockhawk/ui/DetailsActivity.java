package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by RamY on 20/05/16.
 */
public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String stockName="";
    private LineChartView lineChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_line_graph);
        lineChartView = (LineChartView) findViewById(R.id.linechart);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("stock")) {
                stockName=intent.getStringExtra("stock");
                Log.v("Got the Stock :", intent.getStringExtra("stock"));
            }
            setTitle(stockName.toUpperCase());
        }
        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{stockName},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() != 0)
            renderGraph(data);


    }

    public void renderGraph(Cursor data) {
        LineSet dataSet = new LineSet();
        float price=0;
        float maxPrice=0;
        float minPrice=0;

        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            String label = data.getString(data.getColumnIndexOrThrow(QuoteColumns.BIDPRICE));
            price = Float.parseFloat(label);
            maxPrice=Math.max(maxPrice,price);
            if(minPrice==0) {
                minPrice = Math.min(maxPrice, price);
            }else {
                minPrice = Math.min(minPrice, price);
            }
            dataSet.addPoint(label, price);
        }

        if (dataSet.size() > 1) {

            dataSet.setColor(Color.parseColor("#AB47BC"))
                    .setFill(Color.parseColor("#81D4FA"))
                    .setDotsColor(Color.parseColor("#9C27B0"))
                    .setThickness(2)
                    .setDotsRadius(10);


            lineChartView.setXLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(Color.parseColor("#673AB7"))
                    .setAxisBorderValues(Math.round(Math.round(minPrice - 5f)), Math.round(maxPrice + 5f))
                    .addData(dataSet);

            Animation anim = new Animation();
            lineChartView.show(anim);
        }
        else
            Utils.sendMessage(getString(R.string.insufficient_graph_data),this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
