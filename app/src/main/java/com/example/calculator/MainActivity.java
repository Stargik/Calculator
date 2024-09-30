package com.example.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Spinner matrixSpinner = findViewById(R.id.matrixSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.matrix_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matrixSpinner.setAdapter(adapter);
        final int[] matrixDem = {2};
        matrixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                cleanAnswers();
                if (selectedItem.equals("2x2")){
                    findViewById(R.id.editTextNumber13).setVisibility(View.GONE);
                    findViewById(R.id.editTextNumber23).setVisibility(View.GONE);
                    findViewById(R.id.editTextNumber31).setVisibility(View.GONE);
                    findViewById(R.id.editTextNumber32).setVisibility(View.GONE);
                    findViewById(R.id.editTextNumber33).setVisibility(View.GONE);
                    findViewById(R.id.editTextNumber34).setVisibility(View.GONE);
                    findViewById(R.id.textView7).setVisibility(View.GONE);
                    findViewById(R.id.textViewRes3).setVisibility(View.GONE);
                    findViewById(R.id.textViewResx3).setVisibility(View.GONE);
                    findViewById(R.id.textViewRes6).setVisibility(View.GONE);
                    findViewById(R.id.textViewResMatrixx3).setVisibility(View.GONE);
                    matrixDem[0] = 2;
                }
                if (selectedItem.equals("3x3")){
                    findViewById(R.id.editTextNumber13).setVisibility(View.VISIBLE);
                    findViewById(R.id.editTextNumber23).setVisibility(View.VISIBLE);
                    findViewById(R.id.editTextNumber31).setVisibility(View.VISIBLE);
                    findViewById(R.id.editTextNumber32).setVisibility(View.VISIBLE);
                    findViewById(R.id.editTextNumber33).setVisibility(View.VISIBLE);
                    findViewById(R.id.editTextNumber34).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewRes3).setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewResx3).setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewRes6).setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewResMatrixx3).setVisibility(View.VISIBLE);
                    findViewById(R.id.lineChart).setVisibility(View.GONE);
                    matrixDem[0] = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (matrixDem[0] == 2) {
                        calculateEquation2x2();
                    }
                    if (matrixDem[0] == 3) {
                        calculateEquation3x3();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Data is not valid" , Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void calculateEquation2x2(){
        double[][] coefs = new double[2][3];
        coefs[0][0] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber11)).getText().toString());
        coefs[0][1] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber12)).getText().toString());
        coefs[0][2] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber14)).getText().toString());
        coefs[1][0] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber21)).getText().toString());
        coefs[1][1] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber22)).getText().toString());
        coefs[1][2] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber24)).getText().toString());
        final TextView x11 = (TextView) findViewById(R.id.textViewResx1);
        final TextView x21 = (TextView) findViewById(R.id.textViewResx2);
        final TextView x12 = (TextView) findViewById(R.id.textViewResMatrixx1);
        final TextView x22 = (TextView) findViewById(R.id.textViewResMatrixx2);
        final TextView gaussTime = (TextView) findViewById(R.id.textViewTimeRes);
        final TextView matrixTime = (TextView) findViewById(R.id.textViewTimeResMatrix);
        DecimalFormat df = new DecimalFormat("#.####");
        long startTime = System.nanoTime();
        double[] resGauss = calculateEquationGauss(coefs, 2);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        gaussTime.setText(executionTime + " ns");
        startTime = System.nanoTime();
        double[] resMatrix = calculateEquationMatrix(coefs, 2);
        endTime = System.nanoTime();
        executionTime = endTime - startTime;
        matrixTime.setText(executionTime + " ns");
        x11.setText(df.format(resGauss[0]));
        x21.setText(df.format(resGauss[1]));
        x12.setText(df.format(resMatrix[0]));
        x22.setText(df.format(resMatrix[1]));
        drawChart(coefs, resGauss[0], resGauss[1]);
    }

    private void calculateEquation3x3(){
        double[][] coefs = new double[3][4];
        coefs[0][0] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber11)).getText().toString());
        coefs[0][1] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber12)).getText().toString());
        coefs[0][2] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber13)).getText().toString());
        coefs[0][3] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber14)).getText().toString());
        coefs[1][0] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber21)).getText().toString());
        coefs[1][1] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber22)).getText().toString());
        coefs[1][2] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber23)).getText().toString());
        coefs[1][3] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber24)).getText().toString());
        coefs[2][0] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber31)).getText().toString());
        coefs[2][1] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber32)).getText().toString());
        coefs[2][2] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber33)).getText().toString());
        coefs[2][3] = Double.parseDouble(((EditText)findViewById(R.id.editTextNumber34)).getText().toString());
        final TextView x11 = (TextView) findViewById(R.id.textViewResx1);
        final TextView x21 = (TextView) findViewById(R.id.textViewResx2);
        final TextView x31 = (TextView) findViewById(R.id.textViewResx3);
        final TextView x12 = (TextView) findViewById(R.id.textViewResMatrixx1);
        final TextView x22 = (TextView) findViewById(R.id.textViewResMatrixx2);
        final TextView x32 = (TextView) findViewById(R.id.textViewResMatrixx3);
        final TextView gaussTime = (TextView) findViewById(R.id.textViewTimeRes);
        final TextView matrixTime = (TextView) findViewById(R.id.textViewTimeResMatrix);
        DecimalFormat df = new DecimalFormat("#.####");
        long startTime = System.currentTimeMillis();
        double[] resGauss = calculateEquationGauss(coefs, 3);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        gaussTime.setText(df.format(executionTime));
        startTime = System.currentTimeMillis();
        double[] resMatrix = calculateEquationMatrix(coefs, 3);
        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        matrixTime.setText(df.format(executionTime));
        x11.setText(df.format(resGauss[0]));
        x21.setText(df.format(resGauss[1]));
        x31.setText(df.format(resGauss[2]));
        x12.setText(df.format(resMatrix[0]));
        x22.setText(df.format(resMatrix[1]));
        x32.setText(df.format(resMatrix[2]));
    }

    private void cleanAnswers(){
        final TextView x11 = (TextView) findViewById(R.id.textViewResx1);
        final TextView x21 = (TextView) findViewById(R.id.textViewResx2);
        final TextView x31 = (TextView) findViewById(R.id.textViewResx3);
        final TextView x12 = (TextView) findViewById(R.id.textViewResMatrixx1);
        final TextView x22 = (TextView) findViewById(R.id.textViewResMatrixx2);
        final TextView x32 = (TextView) findViewById(R.id.textViewResMatrixx3);
        x11.setText("");
        x21.setText("");
        x31.setText("");
        x12.setText("");
        x22.setText("");
        x32.setText("");
    }

    private  void drawChart(double[][] coefs, double resultX, double resultY){
        LineChart lineChart = findViewById(R.id.lineChart);
        lineChart.setVisibility(View.VISIBLE);
        ArrayList<Entry> entries1 = new ArrayList<>();
        if (coefs[0][1] != 0){
            for (int x = (int)resultX - 10; x <= (int)resultX + 10; x++) {
                float x2 = (float) ((coefs[0][2] - x * coefs[0][0]) / coefs[0][1]);
                entries1.add(new Entry(x, x2));
            }
        }else{
            float x1 = (float) ((coefs[0][2]) / coefs[0][0]);
            for (int x = (int)resultY - 10; x <= (int)resultY + 10; x++) {
                entries1.add(new Entry(x1, x));
            }
        }
        LineDataSet dataSet1 = new LineDataSet(entries1, "1");
        dataSet1.setColor(R.color.purple);
        dataSet1.setLineWidth(2f);
        dataSet1.setDrawCircles(false);
        dataSet1.setDrawValues(false);

        ArrayList<Entry> entries2 = new ArrayList<>();
        if (coefs[0][1] != 0) {
            for (int x = (int) resultX - 10; x <= (int) resultX + 10; x++) {
                float x2 = (float) ((coefs[1][2] - x * coefs[1][0]) / coefs[1][1]);
                entries2.add(new Entry(x, x2));
            }
        }else{
            float x1 = (float) ((coefs[1][2]) / coefs[1][0]);
            for (int x = (int)resultY - 10; x <= (int)resultY + 10; x++) {
                entries1.add(new Entry(x1, x));
            }
        }
        LineDataSet dataSet2 = new LineDataSet(entries2, "2");
        dataSet2.setColor(R.color.purple);
        dataSet2.setLineWidth(2f);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawValues(false);

        LineData lineData = new LineData(dataSet1, dataSet2);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private double[] calculateEquationGauss(double[][] coefs, int dem) {
        double[][] A = new double[dem][dem];
        double[] b = new double[dem];
        for (int i = 0; i < dem; i++){
            System.arraycopy(coefs[i], 0, A[i], 0, dem);
            b[i] = coefs[i][dem];
        }

        int N  = A.length;
        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double t = b[p]; b[p] = b[max]; b[max] = t;

            if (Math.abs(A[p][p]) <= 1e-10) {
                System.out.println("NO");
                return new double[dem];
            }

            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
    private double[] calculateEquationMatrix(double[][] coefs, int dem) {
        double[][] A = new double[dem][dem];
        double[] b = new double[dem];
        for (int i = 0; i < dem; i++){
            System.arraycopy(coefs[i], 0, A[i], 0, dem);
            b[i] = coefs[i][dem];
        }

        int N  = A.length;

        double temp;

        double[][] E = new double[N][N];


        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            {
                E[i][j] = 0f;

                if (i == j)
                    E[i][j] = 1f;
            }

        for (int k = 0; k < N; k++)
        {
            temp = A[k][k];

            for (int j = 0; j < N; j++)
            {
                A[k][j] /= temp;
                E[k][j] /= temp;
            }

            for (int i = k + 1; i < N; i++)
            {
                temp = A[i][k];

                for (int j = 0; j < N; j++)
                {
                    A[i][j] -= A[k][j] * temp;
                    E[i][j] -= E[k][j] * temp;
                }
            }
        }

        for (int k = N - 1; k > 0; k--) {
            for (int i = k - 1; i >= 0; i--) {
                temp = A[i][k];

                for (int j = 0; j < N; j++) {
                    A[i][j] -= A[k][j] * temp;
                    E[i][j] -= E[k][j] * temp;
                }
            }
        }

        double[] x = new double[N];
        for (int i = 0; i < N; i++){
            x[i] = 0;
            for (int k = 0; k < b.length; k++){
                x[i] += E[i][k] * b[k];
            }
        }

        return x;
    }
}