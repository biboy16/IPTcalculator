package com.example.iptcalculator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText calculatorEditText;
    private TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculatorEditText = findViewById(R.id.calculatorEditText);
        historyTextView = findViewById(R.id.historyTextView);

        // Set the initial visibility of historyTextView to GONE
        historyTextView.setVisibility(View.GONE);

        GridLayout buttonsGridLayout = findViewById(R.id.buttonsGridLayout);
        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            Button button = new Button(this);
            button.setText(label);
            button.setTextSize(20);
            button.setGravity(Gravity.CENTER);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClick(view);
                }
            });
            buttonsGridLayout.addView(button);
        }
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String currentText = calculatorEditText.getText().toString();

        switch (buttonText) {
            case "=":
                calculateAndAppendHistory(currentText);
                break;
            default:
                calculatorEditText.setText(currentText + buttonText);
                break;
        }
    }
    public void showHistory(View view) {
        if (view.getId() == R.id.historyButton) {
            if (historyTextView.getVisibility() == View.VISIBLE) {
                historyTextView.setVisibility(View.GONE);
            } else {
                historyTextView.setVisibility(View.VISIBLE);

                String history = historyTextView.getText().toString();
                Toast.makeText(this, history, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void calculateAndAppendHistory(String expression) {
        try {
            double result = evaluateExpression(expression);
            String history = historyTextView.getText().toString();
            historyTextView.setText(history + "\n" + expression + " = " + result);
            calculatorEditText.setText(String.valueOf(result));
        } catch (Exception e) {
            calculatorEditText.setText("Error");
            e.printStackTrace();
        }
    }

    private double evaluateExpression(String expression) {
        try {
            return evaluateSimpleExpression(expression);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private double evaluateSimpleExpression(String expression) {
        expression = expression.trim();

        if (expression.contains("+") || expression.contains("-") || expression.contains("*") || expression.contains("/")) {
            String[] tokens = expression.split("((?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\*)|(?=\\*)|(?<=/)|(?=/))");

            if (tokens.length > 0 && (tokens[0].equals("-") || tokens[0].equals("+"))) {
                tokens[1] = tokens[0] + tokens[1];
                tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
            }

            double operand1 = Double.parseDouble(tokens[0]);
            String operator = tokens[1];
            double operand2 = Double.parseDouble(tokens[2]);

            switch (operator) {
                case "+":
                    return operand1 + operand2;
                case "-":
                    return operand1 - operand2;
                case "*":
                    return operand1 * operand2;
                case "/":
                    if (operand2 == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return operand1 / operand2;
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        } else {
            return Double.parseDouble(expression);
        }

    }
    public void clearInput(View view) {
        if (calculatorEditText != null) {
            calculatorEditText.setText("");
        }
    }
}
