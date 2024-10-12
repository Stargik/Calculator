package com.example.calculator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.calculator.databinding.FragmentMainBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    public MainFragment() {
        super(R.layout.fragment_main);
    }

    private FragmentMainBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home);
        binding = FragmentMainBinding.bind(view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.matrix_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.matrixSpinner.setAdapter(adapter);
        final int[] matrixDem = {2};
        binding.matrixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                cleanAnswers();
                if (selectedItem.equals("2x2")){
                    binding.editTextNumber13.setVisibility(View.GONE);
                    binding.editTextNumber23.setVisibility(View.GONE);
                    binding.editTextNumber31.setVisibility(View.GONE);
                    binding.editTextNumber32.setVisibility(View.GONE);
                    binding.editTextNumber33.setVisibility(View.GONE);
                    binding.editTextNumber34.setVisibility(View.GONE);
                    binding.textView7.setVisibility(View.GONE);
                    binding.textViewRes3.setVisibility(View.GONE);
                    binding.textViewResx3.setVisibility(View.GONE);
                    binding.textViewRes6.setVisibility(View.GONE);
                    binding.textViewResMatrixx3.setVisibility(View.GONE);
                    matrixDem[0] = 2;
                }
                if (selectedItem.equals("3x3")){
                    binding.editTextNumber13.setVisibility(View.VISIBLE);
                    binding.editTextNumber23.setVisibility(View.VISIBLE);
                    binding.editTextNumber31.setVisibility(View.VISIBLE);
                    binding.editTextNumber32.setVisibility(View.VISIBLE);
                    binding.editTextNumber33.setVisibility(View.VISIBLE);
                    binding.editTextNumber34.setVisibility(View.VISIBLE);
                    binding.textView7.setVisibility(View.VISIBLE);
                    binding.textViewRes3.setVisibility(View.VISIBLE);
                    binding.textViewResx3.setVisibility(View.VISIBLE);
                    binding.textViewRes6.setVisibility(View.VISIBLE);
                    binding.textViewResMatrixx3.setVisibility(View.VISIBLE);
                    binding.lineChart.setVisibility(View.GONE);
                    matrixDem[0] = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (matrixDem[0] == 2) {
                        calculateEquation2x2();
                    }
                    if (matrixDem[0] == 3) {
                        calculateEquation3x3();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Data is not valid" , Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (matrixDem[0] == 2) {
                        double[][] coefs = getCoefFromFile(2);
                        writeCoefs(coefs, 2);
                    }
                    if (matrixDem[0] == 3) {
                        double[][] coefs = getCoefFromFile(3);
                        writeCoefs(coefs, 3);
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Data is not valid" , Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void calculateEquation2x2(){
        double[][] coefs = new double[2][3];
        coefs[0][0] = Double.parseDouble(binding.editTextNumber11.getText().toString());
        coefs[0][1] = Double.parseDouble(binding.editTextNumber12.getText().toString());
        coefs[0][2] = Double.parseDouble(binding.editTextNumber14.getText().toString());
        coefs[1][0] = Double.parseDouble(binding.editTextNumber21.getText().toString());
        coefs[1][1] = Double.parseDouble(binding.editTextNumber22.getText().toString());
        coefs[1][2] = Double.parseDouble(binding.editTextNumber24.getText().toString());
        final TextView x11 = binding.textViewResx1;
        final TextView x21 = binding.textViewResx2;
        final TextView x12 = binding.textViewResMatrixx1;
        final TextView x22 = binding.textViewResMatrixx2;
        final TextView gaussTime = binding.textViewTimeRes;
        final TextView matrixTime = binding.textViewTimeResMatrix;
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
        saveCoefsIntoFile(coefs, 2);
    }

    private void calculateEquation3x3(){
        double[][] coefs = new double[3][4];
        coefs[0][0] = Double.parseDouble(binding.editTextNumber11.getText().toString());
        coefs[0][1] = Double.parseDouble(binding.editTextNumber12.getText().toString());
        coefs[0][2] = Double.parseDouble(binding.editTextNumber13.getText().toString());
        coefs[0][3] = Double.parseDouble(binding.editTextNumber14.getText().toString());
        coefs[1][0] = Double.parseDouble(binding.editTextNumber21.getText().toString());
        coefs[1][1] = Double.parseDouble(binding.editTextNumber22.getText().toString());
        coefs[1][2] = Double.parseDouble(binding.editTextNumber23.getText().toString());
        coefs[1][3] = Double.parseDouble(binding.editTextNumber24.getText().toString());
        coefs[2][0] = Double.parseDouble(binding.editTextNumber31.getText().toString());
        coefs[2][1] = Double.parseDouble(binding.editTextNumber32.getText().toString());
        coefs[2][2] = Double.parseDouble(binding.editTextNumber33.getText().toString());
        coefs[2][3] = Double.parseDouble(binding.editTextNumber34.getText().toString());
        final TextView x11 = binding.textViewResx1;
        final TextView x21 = binding.textViewResx2;
        final TextView x31 = binding.textViewResx3;
        final TextView x12 = binding.textViewResMatrixx1;
        final TextView x22 = binding.textViewResMatrixx2;
        final TextView x32 = binding.textViewResMatrixx3;
        final TextView gaussTime = binding.textViewTimeRes;
        final TextView matrixTime = binding.textViewTimeResMatrix;
        DecimalFormat df = new DecimalFormat("#.####");
        long startTime = System.nanoTime();
        double[] resGauss = calculateEquationGauss(coefs, 3);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        gaussTime.setText(executionTime + " ns");
        startTime = System.nanoTime();
        double[] resMatrix = calculateEquationMatrix(coefs, 3);
        endTime = System.nanoTime();
        executionTime = endTime - startTime;
        matrixTime.setText(executionTime + " ns");
        x11.setText(df.format(resGauss[0]));
        x21.setText(df.format(resGauss[1]));
        x31.setText(df.format(resGauss[2]));
        x12.setText(df.format(resMatrix[0]));
        x22.setText(df.format(resMatrix[1]));
        x32.setText(df.format(resMatrix[2]));
        saveCoefsIntoFile(coefs, 3);
    }

    private void cleanAnswers(){
        final TextView x11 = binding.textViewResx1;
        final TextView x21 = binding.textViewResx2;
        final TextView x31 = binding.textViewResx3;
        final TextView x12 = binding.textViewResMatrixx1;
        final TextView x22 = binding.textViewResMatrixx2;
        final TextView x32 = binding.textViewResMatrixx3;
        x11.setText("");
        x21.setText("");
        x31.setText("");
        x12.setText("");
        x22.setText("");
        x32.setText("");
    }

    private  void drawChart(double[][] coefs, double resultX, double resultY){
        LineChart lineChart = binding.lineChart;
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

    private void saveCoefsIntoFile(double[][] coefs, int dem){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefs.length; i++) {
            for (int j = 0; j < coefs[i].length; j++) {
                sb.append(coefs[i][j]);
                if (j != coefs[i].length - 1) {
                    sb.append(" ");
                }
            }
            if (i != coefs.length - 1) {
                sb.append("\n");
            }
        }
        String result = sb.toString();
        String filename;
        if (dem == 3){
            filename = getString(R.string.coefs3_txt);
        }else{
            filename = getString(R.string.coefs2_txt);
        }
        try (FileOutputStream fos = requireActivity().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(result.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private double[][] getCoefFromFile(int dem){
        String filename;
        if (dem == 3){
            filename = getString(R.string.coefs3_txt);
        }else{
            filename = getString(R.string.coefs2_txt);
        }
        List<double[]> rows = new ArrayList<>();
        double[][] coefs = new double[dem][dem + 1];

        try (FileInputStream fis = requireActivity().openFileInput(filename)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] stringValues = line.trim().split(" ");
                    double[] row = new double[stringValues.length];
                    for (int i = 0; i < stringValues.length; i++) {
                        row[i] = Double.parseDouble(stringValues[i]);
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < dem; i++) {
            coefs[i] = rows.get(i);
        }
        return coefs;
    }
    private void writeCoefs(double[][] coefs, int dem){
        if (dem == 2){
            binding.editTextNumber11.setText(String.valueOf(coefs[0][0]));
            binding.editTextNumber12.setText(String.valueOf(coefs[0][1]));
            binding.editTextNumber14.setText(String.valueOf(coefs[0][2]));
            binding.editTextNumber21.setText(String.valueOf(coefs[1][0]));
            binding.editTextNumber22.setText(String.valueOf(coefs[1][1]));
            binding.editTextNumber24.setText(String.valueOf(coefs[1][2]));
        }else{
            binding.editTextNumber11.setText(String.valueOf(coefs[0][0]));
            binding.editTextNumber12.setText(String.valueOf(coefs[0][1]));
            binding.editTextNumber13.setText(String.valueOf(coefs[0][2]));
            binding.editTextNumber14.setText(String.valueOf(coefs[0][3]));
            binding.editTextNumber21.setText(String.valueOf(coefs[1][0]));
            binding.editTextNumber22.setText(String.valueOf(coefs[1][1]));
            binding.editTextNumber23.setText(String.valueOf(coefs[1][2]));
            binding.editTextNumber24.setText(String.valueOf(coefs[1][3]));
            binding.editTextNumber31.setText(String.valueOf(coefs[2][0]));
            binding.editTextNumber32.setText(String.valueOf(coefs[2][1]));
            binding.editTextNumber33.setText(String.valueOf(coefs[2][2]));
            binding.editTextNumber34.setText(String.valueOf(coefs[2][3]));
        }
    }
}
