package com.example.absencesessect.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.absencesessect.R;
import com.example.absencesessect.models.FileData;
import com.example.absencesessect.utils.ExcelReader;
import com.example.absencesessect.utils.PDFReader;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import android.content.Intent;
import android.provider.Settings;

public class TimetableActivity extends AppCompatActivity {

    private Button uploadExcelButton;
    private Button uploadPDFButton;
    private TextView fileContentTextView;
    private FirebaseFirestore db;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private ActivityResultLauncher<Intent> manageStoragePermissionLauncher;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();
                    if (fileUri != null) {
                        String fileType = getContentResolver().getType(fileUri);
                        if (fileType != null) {
                            if (fileType.equals("application/vnd.ms-excel") || fileType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                                handleFileUpload(fileUri, "Excel");
                            } else if (fileType.equals("application/pdf")) {
                                handleFileUpload(fileUri, "PDF");
                            } else {
                                Toast.makeText(this, "Invalid file type selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        uploadExcelButton = findViewById(R.id.uploadExcelButton);
        uploadPDFButton = findViewById(R.id.uploadPDFButton);
        fileContentTextView = findViewById(R.id.fileContentTextView);
        db = FirebaseFirestore.getInstance();


        manageStoragePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                        Toast.makeText(TimetableActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(TimetableActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                manageStoragePermissionLauncher.launch(intent);
            }
        } else {
        }

        uploadExcelButton.setOnClickListener(v -> {
            Intent excelIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            excelIntent.setType("application/vnd.ms-excel");
            excelIntent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(excelIntent);
        });

        uploadPDFButton.setOnClickListener(v -> {
            Intent pdfIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pdfIntent.setType("application/pdf");
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(pdfIntent);
        });


        loadFiles();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadFiles() {
        db.collection("timetables").orderBy("uploadTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder fileList = new StringBuilder();
                        for (DocumentSnapshot document : task.getResult()) {
                            FileData fileData = document.toObject(FileData.class);

                            if (fileData != null) {
                                fileList.append("File Type: ").append(fileData.getFileType()).append("\n");
                                fileList.append("Content (Preview): ").append(fileData.getFileContent().substring(0, Math.min(fileData.getFileContent().length(), 100)))
                                        .append("...\n\n");
                            }
                        }

                        if (fileList.length() > 0) {
                            fileContentTextView.setText(fileList.toString());
                        } else {
                            fileContentTextView.setText("No files uploaded yet.");
                        }
                    } else {
                        Toast.makeText(TimetableActivity.this, "Error loading files", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFileUpload(Uri fileUri, String fileType) {

        String filePath = getRealPathFromURI(fileUri);

        if (filePath != null) {
            if (fileType.equals("PDF")) {

                PDFReader.readPDF(filePath, data -> {

                    uploadFileToFirestore(data, fileType);
                });
            } else if (fileType.equals("Excel")) {

                ExcelReader.readExcel(filePath, data -> {

                    String excelContent = String.join("\n", data);
                    uploadFileToFirestore(excelContent, fileType);
                });
            }
        }
    }

    private void uploadFileToFirestore(String content, String fileType) {

        FileData fileData = new FileData(content, fileType, FieldValue.serverTimestamp());
        db.collection("timetables")
                .add(fileData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    loadFiles();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error uploading file", Toast.LENGTH_SHORT).show();
                });
    }

    private String getRealPathFromURI(Uri uri) {
        String path = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1) {
                    path = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }

        return path;
    }
}
