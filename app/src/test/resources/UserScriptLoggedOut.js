if (!window.jQuery) { document.getElementById('noscript-warning').style.display = 'block'; }
$('#noscript-warning').hide();
try {
    // Set up tag lists for client-side image spoilering/hiding
    window.booru.filterID = 100073;
    window.booru.filterCanEdit = false;
    window.booru.hiddenTagList = [61060,89379,20417,86723,94478,25605,25724,26707,188629,27611,27620,27793,27860,29220,29266,29300,30840,36299,36440,36699,38204,39068,44359,48119];
    window.booru.spoileredTagList = [41133,41161,42773,114937,173118,173119,173120,173121,173122,173123,173124];
    window.booru.hiddenFilter = window.booru.parseSearch("");
    window.booru.spoileredFilter = window.booru.parseSearch("");
    window.booru.ignoredTagList = [];
    window.booru.watchedTagList = [];
    window.booru.userID = "null";
    window.booru.userSlug = "null";
    window.booru.userName = "null";
    window.booru.userAvatar = "//derpicdn.net/assets/no_avatar.svg";
    window.booru.spoilerType = "static";
    window.booru._hidden_tag = "//derpicdn.net/assets/tagblocked-a6790e31ad2d0ec2fdd400aab8ba2eb7177c0489c12525bfda9ef42be933e662.svg";
    window.booru._interactions = [];
    window.booru.enable_browser_ponies = true;
    // CDN root
    window.booru._cdnHost = "//derpicdn.net"
    // Image root
    window.booru._imgHost = "//derpicdn.net/img"
    // Fetch tag metadata and set up filtering
    window.booru.initializeFilters();
    // Show buttons on this user's owned objects
    window.booru.showOwnedObjects();
    // Mouseover and other events
    window.booru.images.bindEvents();
} catch(e) {
    // Bubble up to error console for users
    throw e;
}