edflow

    .controller("edflowController", function ($scope, $http) {

        $scope.lang = "en";
        //$scope.lang = "ru";

        $("#search").keyup(function (e) {
            if (e.keyCode == 13) {
                var category = {};
                category.title = $("#search").val();
                $scope.select(category);
            }
        });

        $scope.selectedCategories = [];
        $scope.subCategories = [];

        $scope.select = function(category) {

            if (category !== "")
                $scope.selectedCategories.push(category);

            $scope.subCategories = [];

            $http.get("api/category?category=" + category.title).success(function (data) {

                    $scope.pages = data.pages;
                    $scope.subCategories = data.subCategories;

                    $scope.moveMenu();
                })
                .error(function (e) {
                    console.log(e);
                });
        }

        $scope.select("");

        $scope.deselect = function(category) {

            while ($scope.selectedCategories.pop() !== category) {
            }

            if ($scope.selectedCategories.length == 0)
                $scope.select("");
            else
                $scope.select($scope.selectedCategories.pop());

        }

        var getLongest = function(arr) {

            var length = 0;
            var longest;

            for (var i = 0; i < arr.length; i++){

                if (arr[i].title.length > length) {

                    var length = arr[i].title.length;
                    longest = arr[i];
                }
            }

            return longest;
        }

        $scope.moveMenu = function() {

            //var biggest = $scope.subCategories.reduce(function (a, b) { return a.length < b.length ? a : b; });
            //var longest = $scope.subCategories.sort(function (a, b) { return b.length - a.length; })[0];

            var longest = getLongest($scope.subCategories);
            //console.log(longest.title.length);

            var menu = longest.title.length * 8;
            console.log(longest.title + " " + longest.title.length);

            $scope.mWidth = (45 + menu) + "px";
            $scope.cWidth = "calc(100% - " + $scope.mWidth + ")";
            //console.log(document.getElementById("menu").offsetWidth)
        }
    })

