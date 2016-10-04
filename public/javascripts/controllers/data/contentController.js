edflow

    .controller("contentController", function ($rootScope, $state) {

        $rootScope.favicon = function (url) {

            // console.log(url)

            url = "http://" + url.split("://")[1].split("/")[0] + "/favicon.ico";

            // console.log(url)

            return url
        }

        $rootScope.imgError = function() {

            image.onerror = "";
            image.src = "/images/noimage.gif";

            console.log(image);
            return true;
        }

        $rootScope.showText = true;

        $rootScope.showCategories = true;

        $rootScope.showImage = false;

        $rootScope.clickItem = function() {
            console.log("content item");
        }

        $rootScope.moreCategories = function() {
            console.log("item categories more");
        }
    })
