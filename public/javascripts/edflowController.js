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
    })

