package il.ac.technion.socialcampus;

import il.ac.technion.socialcampus.TagsBoxFragment.ClickableString;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;

public class SpanLinkFuctory {
	
	static SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
	    SpannableString link = new SpannableString(text);
	    link.setSpan(new ClickableString(listener), 0, text.length(), 
	        SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    return link;
	}
	
	private static class ClickableString extends ClickableSpan {  
	    private View.OnClickListener mListener;          
	    public ClickableString(View.OnClickListener listener) {              
	        mListener = listener;  
	    }          
	    @Override  
	    public void onClick(View v) {  
	        mListener.onClick(v);  
	    }        
	}

}
