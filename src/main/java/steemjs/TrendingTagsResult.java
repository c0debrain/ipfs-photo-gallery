package steemjs;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative=true)
public abstract class TrendingTagsResult {
	@JsProperty
	public abstract String getName();
	@JsProperty
	public abstract String getTotal_payouts();
	@JsProperty
	public abstract int getNet_votes();
	@JsProperty
	public abstract int getTop_posts();
	@JsProperty
	public abstract int getComments();
	@JsProperty
	public abstract String getTrending();
}