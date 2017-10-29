package muksihs.ipfs.photogallery.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import muksihs.ipfs.photogallery.shared.ImageData;
import muksihs.ipfs.photogallery.shared.IpfsGateway;
import muksihs.ipfs.photogallery.ui.GlobalEventBus;
import muksihs.ipfs.photogallery.ui.Presenter;
import muksihs.ipfs.photogallery.ui.Presenter.ShowView;
import muksihs.ipfs.photogallery.ui.Presenter.View;

public class PhotoGalleryWizard implements ScheduledCommand, GlobalEventBus {
	
	interface MyEventBinder extends EventBinder<PhotoGalleryWizard> {}
	
	private static final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	public PhotoGalleryWizard() {
		eventBinder.bindEventHandlers(this, eventBus);
		IpfsGatewayCache.get();
		new Presenter();
	}

	@Override
	public void execute() {
		fireEvent(new Event.AppLoaded());
		fireEvent(new ShowView(View.Loading));
		Scheduler.get().scheduleFixedDelay(() -> {
			boolean ready = IpfsGateway.isReady();
			if (ready) {
				fireEvent(new Event.IpfsGatewayReady());
				fireEvent(new ShowView(View.SelectImages));
			}
			return !ready;
		}, 250);
	}
	
	@EventHandler
	protected void addImages(Event.AddImages event) {
		if (event.getFiles()==null||event.getFiles().length==0) {
			return;
		}
		fireEvent(new Event.EnableSelectImages(false));
		new LoadFileImages().load(event.getFiles());
	}
	
	@EventHandler
	protected void removeImageFromList(Event.RemoveImage event) {
		if (event.getIndex()<0 || event.getIndex()>=imageDataUrls.size()) {
			return;
		}
		imageDataUrls.remove(event.getIndex());
		fireEvent(new Event.UpdateImageCount(imageDataUrls.size()));
	}
	
	private List<ImageData> imageDataUrls=new ArrayList<>();
	@EventHandler
	protected void imageDataAdded(Event.ImageDataAdded event) {
		imageDataUrls.add(event.getDataUrls());
		fireEvent(new Event.AddToPreviewPanel(event.getDataUrls().getThumbDataUrl(), event.getDataUrls().getCaption()));
		fireEvent(new Event.UpdateImageCount(imageDataUrls.size()));
	}
	
	@EventHandler
	protected void getAppVersion(Event.GetAppVersion event) {
		fireEvent(new Event.DisplayAppVersion("20171029"));
	}
	
	@EventHandler
	protected void addImagesDone(Event.AddImagesDone event) {
		fireEvent(new Event.EnableSelectImages(true));
	}
}