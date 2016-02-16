edflow

    .controller("edflowController", function ($scope, $rootScope, $http) {

        $rootScope.menuWidth = "50%";
        $rootScope.contentWidth = "50%";
        $rootScope.circleLeft = "calc(50% - 135px)";
        $rootScope.categoriesTop = "calc(50% - 26px)";

        $("#search").keyup(function (e) {
            if (e.keyCode == 13) {
                var category = {};
                category.title = $("#search").val();
                $scope.select(category);
            }
        });

        //$scope.selectedCategories = [];
        //$scope.subCategories = [];
        //
        //$scope.selectCategory = function(category) {
        //
        //    if (category !== "")
        //        $scope.selectedCategories.push(category);
        //
        //    $scope.subCategories = [];
        //
        //    $http.get("api/category?category=" + category.title + "&language=" + $scope.language).success(function (data) {
        //
        //            $scope.items = data.items;
        //            $scope.subCategories = data.subCategories;
        //
        //            $scope.moveMenu();
        //        })
        //        .error(function (e) {
        //            console.log(e);
        //        });
        //}
        //
        //$scope.selectCategory("");
        //
        //$scope.deselectCategory = function(category) {
        //
        //    while ($scope.selectedCategories.pop() !== category) {
        //    }
        //
        //    if ($scope.selectedCategories.length == 0)
        //        $scope.selectCategory("");
        //    else
        //        $scope.selectCategory($scope.selectedCategories.pop());
        //
        //}

        $rootScope.getLongest = function (arr) {

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

        $rootScope.getTextWidth = function (text, font) {

            // re-use canvas object for better performance
            var canvas = $rootScope.getTextWidth.canvas || ($rootScope.getTextWidth.canvas = document.createElement("canvas"));
            var context = canvas.getContext("2d");
            context.font = font;
            var metrics = context.measureText(text);
            return metrics.width;
        };
    })

