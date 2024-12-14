package com.example.absencesessect.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.views.AdminPanelActivity;
import com.example.absencesessect.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private FirebaseFirestore db;

    public AdminAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.userName.setText("Name: " + user.getName());
        holder.userEmail.setText("Email: " + user.getEmail());

        holder.deleteUserButton.setOnClickListener(v -> showDeleteConfirmationDialog(user.getUserId(), position));

        holder.assignRoleButton.setOnClickListener(v -> showRoleAssignmentDialog(user.getUserId()));

        // When a user is clicked, show absences for that teacher
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof AdminPanelActivity) {
                ((AdminPanelActivity) context).showTeacherAbsences(user.getEmail());
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void showDeleteConfirmationDialog(String userId, int position) {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUser(userId, position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUser(String userId, int position) {
        db.collection("users").document(userId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.remove(position);
                notifyItemRemoved(position);
                Snackbar.make(((AdminPanelActivity) context).findViewById(android.R.id.content),
                        "User deleted successfully", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(((AdminPanelActivity) context).findViewById(android.R.id.content),
                        "Error deleting user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showRoleAssignmentDialog(String userId) {
        final String[] roles = {"Admin", "Teacher", "Agent"};
        new AlertDialog.Builder(context)
                .setTitle("Assign Role")
                .setItems(roles, (dialog, which) -> assignRoleToUser(userId, roles[which]))
                .show();
    }

    private void assignRoleToUser(String userId, String role) {
        db.collection("users").document(userId).update("role", role).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(((AdminPanelActivity) context).findViewById(android.R.id.content),
                        "Role assigned successfully", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(((AdminPanelActivity) context).findViewById(android.R.id.content),
                        "Error assigning role", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAbsencesForTeacher(String userId, RecyclerView absenceRecyclerView) {
        db.collection("absences").whereEqualTo("teacherEmail", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    List<TeacherAbsence> absences = querySnapshot.toObjects(TeacherAbsence.class);
                    TeacherAbsenceAdapter absenceAdapter = new TeacherAbsenceAdapter(context, absences);  // Use TeacherAbsenceAdapter
                    absenceRecyclerView.setAdapter(absenceAdapter);
                } else {
                    Toast.makeText(context, "No absences found for this teacher", Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(((AdminPanelActivity) context).findViewById(android.R.id.content),
                        "Error loading absences", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userEmail;
        Button deleteUserButton, assignRoleButton;
        RecyclerView absenceRecyclerView;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
            assignRoleButton = itemView.findViewById(R.id.assignRoleButton);
            absenceRecyclerView = itemView.findViewById(R.id.absenceRecyclerView);
        }
    }
}
