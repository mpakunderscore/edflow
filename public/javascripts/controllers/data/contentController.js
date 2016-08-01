edflow

    .controller("contentController", function ($rootScope, $state) {

        var path = $state.current.name;

        // $rootScope.selectCategory({title: path});

        // console.log("selectCategory: " + path)

        $rootScope.favicon = function (url) {

            // console.log(url)

            url = "http://" + url.split("://")[1].split("/")[0] + "/favicon.ico";

            // console.log(url)

            return url
        }
    })
