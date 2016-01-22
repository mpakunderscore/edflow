edflow

    .controller("edflowController", function ($scope, $http) {

        $scope.languages = ["EN", "RU"];

        $scope.language = localStorage.getItem("language");

        //console.log($scope.language);

        if ($scope.language === null)
            $scope.language = "EN";

        $scope.selectLanguage = function(language) {
            $scope.language = language;
            localStorage.setItem("language", language);
            $scope.selectedCategories = [];
            $scope.selectCategory("");
        }

        $("#search").keyup(function (e) {
            if (e.keyCode == 13) {
                var category = {};
                category.title = $("#search").val();
                $scope.select(category);
            }
        });

        $scope.selectedCategories = [];
        $scope.subCategories = [];

        $scope.selectCategory = function(category) {

            if (category !== "")
                $scope.selectedCategories.push(category);

            $scope.subCategories = [];

            $http.get("api/category?category=" + category.title + "&language=" + $scope.language).success(function (data) {

                    $scope.pages = data.pages;
                    $scope.subCategories = data.subCategories;

                    $scope.moveMenu();
                })
                .error(function (e) {
                    console.log(e);
                });
        }

        $scope.selectCategory("");

        $scope.deselectCategory = function(category) {

            while ($scope.selectedCategories.pop() !== category) {
            }

            if ($scope.selectedCategories.length == 0)
                $scope.selectCategory("");
            else
                $scope.selectCategory($scope.selectedCategories.pop());

        }

        var getLongest = function(arr) {

            var length = 0;
            var longest;

            for (var i = 0; i < arr.length; i++){

                if (arr[i].title.length > length) {

                    length = arr[i].title.length;
                    longest = arr[i];
                }
            }

            return longest;
        }

        $scope.moveMenu = function() {

            var longest = getLongest($scope.selectedCategories.concat($scope.subCategories));

            var menu = (getTextWidth(longest.title, "16px Open Sans"));
            console.log(longest.title + " " + menu);

            $scope.mWidth = (44 + menu) + "px";
            $scope.cWidth = "calc(100% - " + $scope.mWidth + ")";
            //console.log(document.getElementById("menu").offsetWidth)
        }

        function getTextWidth(text, font) {

            // re-use canvas object for better performance
            var canvas = getTextWidth.canvas || (getTextWidth.canvas = document.createElement("canvas"));
            var context = canvas.getContext("2d");
            context.font = font;
            var metrics = context.measureText(text);
            return metrics.width;
        };
    })

