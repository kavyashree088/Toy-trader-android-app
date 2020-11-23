package com.example.toytrader;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toytrader.admin.IssueModel;
import com.example.toytrader.admin.ToyModel;
import com.example.toytrader.admin.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FirebaseHelper {

    private final static String TAG = "FIREBASE_HELPER";
    private static FirebaseHelper fireStoreHelper = null;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseListener fbl;
    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseHelper() {
    }

    public static FirebaseHelper getInstance() {
        if (fireStoreHelper == null) {
            fireStoreHelper = new FirebaseHelper();
        }
        return fireStoreHelper;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public void signupWith(String email, String password, final Map data, final FirebaseListener fbl) {
        this.fbl = fbl;
        final Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            setupUserDataAfterSignup(user.getUid(), data, fbl);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            user = null;
                            fbl.getFBData(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void cleanUpForLogout() {
        mAuth.signOut();
    }

    private void setupUserDataAfterSignup(String id, Map userData, final FirebaseListener fbl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        userRef.document(id).set(userData).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                fbl.getFBData(user);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void signinWith(String email, String password, final FirebaseListener fbl) {
        this.fbl = fbl;
        final Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signin:success");
                            user = mAuth.getCurrentUser();
                            fbl.getFBData(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signin:failure", task.getException());
                            user = null;
                            fbl.getFBData(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void uploadToyWithData(Map toyDetails, final Uri selectedImage, @Nullable final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toys");
        toysRef.add(toyDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                uploadToyImage(documentReference.getId(), selectedImage);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fbl.getFBData("Upload Toy failed");
                    }
                });
    }

    public void updateToyData(Map updatedToyDetails, @Nullable final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toys");
        toysRef.document(updatedToyDetails.get("toyid").toString()).update(updatedToyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    fbl.updateFBResult(true);
                } else {
                    fbl.updateFBResult(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.updateFBResult(false);
            }
        });
    }

    public void uploadToyImage(final String toyUID, Uri selectedImage) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final StorageReference ref = storageReference.child("images/" + toyUID);
        ref.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getTask().isSuccessful()) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadPhotoUrl) {
                                    Map m = new HashMap();
                                    m.put("image", downloadPhotoUrl.toString());
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference toysRef = db.collection("toys");
                                    toysRef.document(toyUID).update(m).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                fbl.getFBData(task.isSuccessful());
                                            } else {
                                                fbl.getFBData("Upload image failed");
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            fbl.getFBData("Upload image failed");
                                        }
                                    });
                                }
                            });
                        } else {
                            fbl.getFBData("Upload image failed");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fbl.getFBData("Upload image failed");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());

                    }
                });
    }

    public void getAllToys(final FirebaseListener fbl) {
        this.fbl = fbl;
        final ArrayList<ToyModel> toyList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toys");
        toysRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> json = document.getData();
                        ToyModel toy = new ToyModel((String)json.get("name"), document.getId());
                        toyList.add(toy);
                    }
                    fbl.getFBData(toyList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getAllUsers(final  FirebaseListener fbl){
        this.fbl = fbl;
        final ArrayList<User> userList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Map<String, Object> json = documentSnapshot.getData();
                        User user = new User((String)json.get("first_name")+" "+ (String)json.get("last_name"), documentSnapshot.getId());
                        userList.add(user);
                    }
                    fbl.getFBData(userList);
                }
            }
        });
    }

    public void getAllIssues(final  FirebaseListener fbl){
        this.fbl = fbl;
        final ArrayList<IssueModel> issueModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("toyFrauds");
        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Map<String, Object> json = documentSnapshot.getData();
                        IssueModel issueModel = new IssueModel((String)json.get("toyname"), (String)json.get("reason"), documentSnapshot.getId());
                        issueModels.add(issueModel);
                    }
                    fbl.getFBData(issueModels);
                }
            }
        });
    }

    public void getToysForCategory(String category, final FirebaseListener fbl) {
        this.fbl = fbl;
        final ArrayList<Toy> toyList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toys");
        toysRef.whereEqualTo("category", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> json = document.getData();
                        Toy t = Utilities.generateToyFromJSON(json, document.getId());
                        toyList.add(t);
                    }
                    fbl.getFBData(toyList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getToyForID(String id, final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toys");
        toysRef.document(id).get().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot d = (DocumentSnapshot) task.getResult();
                    Map json = d.getData();
                    Toy t = Utilities.generateToyFromJSON(json, d.getId());
                    fbl.getFBData(t);
                } else {
                    fbl.getFBData(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.getFBData(null);
            }
        });
    }

    public void getIssue(String id, final FirebaseListener fbl) {
        this.fbl = fbl;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference toysRef = db.collection("toyFrauds");
        final HashMap<String, Object> issueDetails = new HashMap<>();
        toysRef.document(id).get().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot d = (DocumentSnapshot) task.getResult();
                    Map<String, Object> json = d.getData();
                    for (Map.Entry<String, Object> entry : json.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        issueDetails.put(key, value);
                    }
                    if( issueDetails.containsKey("reporterid")){
                        String userId = issueDetails.get("reporterid").toString();
                        CollectionReference userRef = db.collection("users");
                        userRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                if(task1.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = (DocumentSnapshot) task1.getResult();
                                    Map<String, Object> userData = documentSnapshot.getData();
                                    issueDetails.put("user_name", userData.get("first_name").toString()
                                            + " " + userData.get("last_name").toString());
                                }
                                fbl.getFBData(issueDetails);
                            }
                        });

                    }

                } else {
                    fbl.getFBData(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.getFBData(null);
            }
        });
    }
    public void getUser(String id, final FirebaseListener fbl){
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       final HashMap<String, Object> userDetails = new HashMap<>();
        CollectionReference usersRef = db.collection("users");
        usersRef.document(id).get().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    Map<String, Object> json = document.getData();
                    for (Map.Entry<String, Object> entry : json.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        userDetails.put(key, value);
                    }
                    fbl.getFBData(userDetails);
                } else {
                    fbl.getFBData(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.getFBData(null);
            }
        });
    }
    public void getDetailsForCurrentUser(final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        userRef.document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot d = (DocumentSnapshot) task.getResult();
                    Map m = d.getData();
                    fbl.getFBData(m);
                } else {
                    fbl.getFBData(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.getFBData(null);
            }
        });
    }

    public void updateDetailsForCurrentUser(Map userData, final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");
        userRef.document(mAuth.getCurrentUser().getUid()).update(userData).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    fbl.updateFBResult(true);
                } else {
                    fbl.updateFBResult(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fbl.updateFBResult(false);
            }
        });
    }

    public void changeUserPassword(String oldPass, final String newPass, final FirebaseListener fbl) {
        this.fbl = fbl;
        user = getFirebaseUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                fbl.getFBData("Success");
                            } else {
                                fbl.getFBData("Failed");
                            }
                        }
                    });
                } else {
                    fbl.getFBData("Wrong Password");
                }
            }
        });
    }

    public void tradeToyWithDetails(Map transactionDetails, final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tradesRef = db.collection("trades");
        tradesRef.add(transactionDetails).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                fbl.getFBData(true);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fbl.getFBData(e);
                    }
                });
    }

    public void returnToyWithID(String transactionID, Map toyUpdates, final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tradesRef = db.collection("trades");
        tradesRef.document(transactionID).update(toyUpdates).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                fbl.getFBData(true);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fbl.getFBData(e);
                    }
                });

    }

    public void reportToy(Map report, final FirebaseListener fbl) {
        this.fbl = fbl;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference fraudsRef = db.collection("toyFrauds");
        fraudsRef.add(report).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                fbl.getFBData(true);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fbl.getFBData(e);
                    }
                });

    }

    public void getMyOrders(final FirebaseListener fbl) {
        this.fbl = fbl;
        final ArrayList<Order> orderList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tradesRef = db.collection("trades");
        tradesRef.whereEqualTo("userid", getFirebaseUser().getUid()).get().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                QuerySnapshot d = (QuerySnapshot) o;
                for (QueryDocumentSnapshot document : d) {
                    Map<String, Object> json = document.getData();
                    Order order = Utilities.generateOrderFromJSON(json, document.getId());
                    orderList.add(order);
                }
                fbl.getFBData(orderList);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fbl.getFBData(e);
                    }
                });
    }
}


