package com.elanelango.apps.twitterjoy.home;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.elanelango.apps.twitterjoy.R;

import org.w3c.dom.Text;

import java.sql.Time;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eelango on 2/21/16.
 */
public class ComposeDialog extends DialogFragment implements Button.OnClickListener{
    public interface ComposeListener {
        void onFinishCompose(String tweet);
    }

    ImageView ivClose;

    TextView tvCount;

    Button btTweet;

    EditText etCompose;

    public ComposeDialog() {
    }

    public static ComposeDialog newInstance(){
        ComposeDialog frag = new ComposeDialog();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_tweet, container);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        btTweet = (Button) view.findViewById(R.id.btTweet);
        etCompose = (EditText) view.findViewById(R.id.etCompose);

        btTweet.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();
                int rem = 140 - len;
                tvCount.setText(Integer.toString(rem));
                if (rem < 0) {
                    tvCount.setTextColor(Color.RED);
                } else {
                    tvCount.setTextColor(Color.BLACK);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCompose.requestFocus();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            ComposeListener listener = (ComposeListener) getActivity();
            dismiss();
            listener.onFinishCompose(etCompose.getText().toString());
        } else if (v instanceof ImageView) {
            dismiss();
        }
    }
}
