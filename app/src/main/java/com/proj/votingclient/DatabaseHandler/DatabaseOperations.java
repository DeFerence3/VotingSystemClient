package com.proj.votingclient.DatabaseHandler;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.HttpHeaders;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
public class DatabaseOperations {
    private final String USERS_COLLECTION = "Users";
    private final String ELECTION_COLLECTION = "Elections";
    CollectionReference userCollectionRef;
    CollectionReference electionCollectionRef;
    String hashedpass;
    String downloadurl;
    String hasheduname;

    /* loaded from: classes10.dex */
    public interface CompletionListener {
        void onCompletion(boolean z);
    }

    public DatabaseOperations() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        userCollectionRef = firestore.collection( USERS_COLLECTION );
        electionCollectionRef = firestore.collection( ELECTION_COLLECTION );
    }

    public boolean createVoter(String uname, String pass, String id, String name, Uri img, CompletionListener listener) {
        hashedpass = hasher(pass);
        hasheduname = hasher(uname);
        return firestoreCreator(hasheduname, hashedpass, id, name, img, listener);
    }

    private String hasher(String code) {
        return new Argon2PasswordEncoder(16, 32, 1, 16384, 2).encode(code);
    }

    boolean firestoreCreator(String username, String password, String vid, String name, Uri img, CompletionListener listener) {
        final Map<String, Object> data = new HashMap<>();
        data.put("Username", username);
        data.put("Password", password);
        data.put("Name", name);
        data.put(HttpHeaders.DATE, getCurrentDate());
        data.put("perms", false);
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("user_profile/" + vid);
        UploadTask uploadTask = storageRef.putFile(img);
        uploadTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                DatabaseOperations.this.m221x7557161e( storageRef, data, vid, listener, (UploadTask.TaskSnapshot) obj);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                exc.printStackTrace();
            }
        });
        return false;
    }

    public /* synthetic */ void m221x7557161e(StorageReference storageRef, final Map<String, Object> data, final String vid, final CompletionListener listener, UploadTask.TaskSnapshot taskSnapshot) {
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                DatabaseOperations.this.m222x78950e60(data, vid, listener, (Uri) obj);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                Log.e("FailedUserProfilePictureUpload" , "Failed");
            }
        });
    }

    public void m222x78950e60(Map<String, Object> data, String vid, CompletionListener listener, Uri uri) {
        this.downloadurl = uri.toString();
        data.put("picture", this.downloadurl);
        firestoreUploader(vid, data, listener);
    }

    private void firestoreUploader(final String vid, Map<String, Object> data, final CompletionListener listener) {
        DocumentReference collectionRef = this.userCollectionRef.document(vid);
        collectionRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseOperations.this.electionUpdate(vid);
                listener.onCompletion(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                listener.onCompletion(false);
            }
        });
    }

    private Object getCurrentDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd , HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void electionUpdate(String vid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference voterDocument = firestore.collection(USERS_COLLECTION).document(vid);
        CollectionReference electionCollection = firestore.collection(ELECTION_COLLECTION);
        electionCollection.get().addOnCompleteListener(new OnCompleteListener() {
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                DatabaseOperations.this.m223xb1d261dc(voterDocument, task);
            }
        });
    }

    public /* synthetic */ void m223xb1d261dc(DocumentReference voterDocument, Task task) {
        if (task.isSuccessful()) {
            Iterator<QueryDocumentSnapshot> it = ((QuerySnapshot) task.getResult()).iterator();
            while (it.hasNext()) {
                DocumentSnapshot doc = it.next();
                String docid = doc.getId();
                Map<String, Object> data = new HashMap<>();
                data.put(docid, 0);
                voterDocument.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override // com.google.android.gms.tasks.OnSuccessListener
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public void onFailure(Exception e) {
                        Log.e("Error", "Error updating document: ", e);
                    }
                });
            }
            return;
        }
        Log.e("Error", "Error getting documents: ", task.getException());
    }

    public Task<QuerySnapshot> getAllElections() {
        return this.electionCollectionRef.get();
    }

    public CollectionReference getAllCollection() {
        return this.electionCollectionRef;
    }

    public CollectionReference getUserCollectionRef() {
        return this.userCollectionRef;
    }
}
