package com.pythonanywhere.brilliantcomputer;

import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author deepakkumar
 */
public class BarChart {
    public ChartPanel createBarChartPanel(){
        JFreeChart chart = ChartFactory.createBarChart(
                "CM Bar Chart",
                "Category",
                "Value",
                getCategDataset(),
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        return chartPanel;
    }  
    public DefaultCategoryDataset getCategDataset()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        MySQLDBFunctions objCm = new MySQLDBFunctions();
        objCm.setRecord("nsecm2023", "cm01aug2023", "*", "WHERE SERIES='EQ' && SYMBOL='INFY'");
        dataset.addValue(1.0D, objCm.symbol.toString(), "cm01aug2023");
        objCm.setRecord("nsecm2023", "cm02aug2023", "*", "WHERE SERIES='EQ' && SYMBOL='INFY'");
        dataset.addValue(6.0D, objCm.symbol.toString(), "cm02aug2023");
        /*
        dataset.addValue(1.0, "Row 1", "Column 1");
        dataset.addValue(5.0, "Row 1", "Column 2");
        dataset.addValue(3.0, "Row 1", "Column 3");
        dataset.addValue(2.0, "Row 2", "Column 1");
        dataset.addValue(3.0, "Row 2", "Column 2");
        dataset.addValue(2.0, "Row 2", "Column 3");
        */
        return dataset;
    }
}
