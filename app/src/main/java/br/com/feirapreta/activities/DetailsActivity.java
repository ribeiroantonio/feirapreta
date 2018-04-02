package br.com.feirapreta.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import br.com.feirapreta.R;
import br.com.feirapreta.model.Post;

public class DetailsActivity extends AppCompatActivity {

    private String postId;
    private SimpleDraweeView postImage;
    private TextView postCaption;
    private TextView postPersonName;
    private TextView postPersonUser;
    private TextView postpersonPhone;
    private Post post;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();
        getPostId();

        // Initializing views.
        initViews();

        if (postId != null) {
            loadPost();
        }
    }

    /**
     * This method is used to recover postId from Extras and to check if the
     * post is a highlight or not.
     */
    private void getPostId() {
        if (bundle.get("post_id") != null) {
            postId = bundle.getString("post_id");
        }
    }

    /**
     * This method is used to initialize the views/components used in the layout of this activity.
     */
    private void initViews() {

        postImage = findViewById(R.id.details_post_image);
        postCaption = findViewById(R.id.details_post_caption);
        postPersonName = findViewById(R.id.details_user_name);
        postPersonUser = findViewById(R.id.details_user_nickname);
        postpersonPhone = findViewById(R.id.details_user_phone);

    }

    /**
     * This method is used to recover the post the user clicked on
     */
    private void loadPost() {
        // checking if the post exists in the bundle.
        if (bundle.get("post") != null) {
            // recovering the post from the bundle of the intent.
            post = (Post) bundle.getSerializable("post");
            // loading the information of the post into the views.
            loadDetails();
        }
    }

    /**
     * This method is used to load the information of the post into the views od this activity.
     */
    private void loadDetails() {

        // checking if the post is null
        if (post != null) {

            //------- Verifying if data isn't null and setting values into the views -------------.
            if (post.getImageLowResolution() != null) {
                postImage.setImageURI(Uri.parse(post.getImageLowResolution()));
                postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(newInstagramPostIntent(getPackageManager(), post.getLink()));
                    }
                });
            }

            if (post.getPerson().getFullNameInstagram() != null && !(post.getPerson()
                    .getFullNameInstagram().isEmpty())) {

                postPersonName.setText(post.getPerson().getFullNameInstagram());
            }

            if (post.getSubtitle() != null) {
                if (post.getSubtitle().replace("?", "").equals("")) {
                    postCaption.setHeight(0);
                } else {
                    postCaption.setMaxLines(6);
                }
                postCaption.setText(post.getSubtitle().replace("?", ""));
                postCaption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(newInstagramPostIntent(getPackageManager(), post.getLink()));
                    }
                });
            }

            if (post.getPerson().getUsernameInstagram() != null) {
                String username = "@" + post.getPerson().getUsernameInstagram();
                postPersonUser.setText(username);
                postPersonUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(newInstagramPostIntent(getPackageManager(),
                                ("https://www.instagram.com/" + post.getPerson()
                                        .getUsernameInstagram())));
                    }
                });

                if (post.getPerson().getPhoneNumber() != null) {
                    postpersonPhone.setText(post.getPerson().getPhoneNumber());
                    postpersonPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phone = post.getPerson().getPhoneNumber();
                            String name = "";
                            if (post.getPerson().getFullNameInstagram() != null) {
                                name = post.getPerson().getFullNameInstagram();
                            }

                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);

                            startActivity(intent);
                        }
                    });
                }

            }
        }
    }

    /**
     * This method is used to check if the user is connected to a network.
     *
     * @return boolean value
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * This method is used to open an Instagram URL in the Instagram app.
     *
     * @param pm  PackageManager of the activity.
     * @param url Instagram URL to be opened.
     * @return Instagram Intent to be started.
     */
    public static Intent newInstagramPostIntent(PackageManager pm, String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                intent.setData(Uri.parse(url));
                intent.setPackage("com.instagram.android");
            }
            return intent;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * This method is used to redirect the user to an activity to create a new contact when the add
     * contact button is clicked.
     *
     * @param view Item to be clicked.
     */
    public void addContact(View view) {
        String phone = "";
        String name = "";

        if (post.getPerson().getPhoneNumber() != null) {
            phone = post.getPerson().getPhoneNumber();
        }

        if (post.getPerson().getFullNameInstagram() != null) {
            name = post.getPerson().getFullNameInstagram();
        }

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);

        startActivity(intent);
    }

    /**
     * This method is used to go back to last activity when the user clicks on back arrow button
     *
     * @param view Item to be clicked
     */
    public void goBackDetails(View view) {
        onBackPressed();
        this.finish();
    }

}
