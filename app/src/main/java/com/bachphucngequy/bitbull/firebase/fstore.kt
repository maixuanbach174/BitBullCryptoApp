//package com.bachphucngequy.bitbull.firebase
//
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QuerySnapshot
//
//
//object FirestoreHelper {
//    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    // Add a document to a collection
//    fun addDocument(collection: String, data: Any, onSuccess: (DocumentReference) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection(collection)
//            .add(data)
//            .addOnSuccessListener { documentReference ->
//                onSuccess(documentReference)
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Retrieve a document by its ID
//    fun getDocument(collection: String, documentId: String, onSuccess: (DocumentSnapshot) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection(collection)
//            .document(documentId)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    onSuccess(document)
//                } else {
//                    onFailure(Exception("Document not found"))
//                }
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Retrieve all documents in a collection
//    fun getAllDocuments(collection: String, onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection(collection)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                onSuccess(querySnapshot)
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Update a document by its ID
//    fun updateDocument(collection: String, documentId: String, updates: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection(collection)
//            .document(documentId)
//            .update(updates)
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//    // Delete a document by its ID
//    fun deleteDocument(collection: String, documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection(collection)
//            .document(documentId)
//            .delete()
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//}