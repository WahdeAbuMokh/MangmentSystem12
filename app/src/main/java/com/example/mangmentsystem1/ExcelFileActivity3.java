package com.example.mangmentsystem1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mangmentsystem1.classes.Matches;
import com.example.mangmentsystem1.classes.Team;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.Iterator;

import static android.content.ContentValues.TAG;

public class ExcelFileActivity3 extends AppCompatActivity {
    Button excel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_file3);
        excel = findViewById(R.id.excel3);

        // click on excel to select a file
        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readExcelFileFromAssets();
            }
        });
    }
    public void readExcelFileFromAssets() {
        try {
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager =getAssets();
            //  open excel sheet
            myInput = assetManager.open("matchExcel.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
            while (rowIter.hasNext()) {
                Log.e(TAG, " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String team1name="", team2name="",team1Key="",team2Key="",date="",time="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            team1name = myCell.toString();
                        }else if (colno==1){
                            team2name = myCell.toString();
                        }else if (colno==2){
                            team1Key = myCell.toString();
                        }else if (colno==3){
                            team2Key = myCell.toString();
                        }else if (colno==4){
                            date = myCell.toString();
                        }else if (colno==5){
                            time = myCell.toString();
                        }
                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    Matches match=new Matches(team1name,team2name,team1Key,team2Key,date,time);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference=firebaseDatabase.getReference("Matches").push();
                    match.setKey(databaseReference.getKey());
                    databaseReference.setValue(match);

                }
                rowno++;
            }
        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }
    }
}