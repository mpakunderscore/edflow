function printBookmarks(bookmarks, i) {

    i++;

    var folder = "-&#160;&#160;&#160;&#160;&#160;&#160;";

    bookmarks.forEach(function(bookmark) {



        if (bookmark.url == null) {

            console.log(bookmark.title)

            // console.debug(bookmark.url);


            if (bookmark.title.length > 0) {

                var div = document.createElement("div");
                div.innerHTML = folder.repeat(i) + "" + bookmark.title;
                document.querySelector('#bookmarks').appendChild(div);
            }

            if (bookmark.children)
                printBookmarks(bookmark.children, i);
        }
    });
}

chrome.bookmarks.getTree(function(bookmarks) {
    printBookmarks(bookmarks, -2);
});