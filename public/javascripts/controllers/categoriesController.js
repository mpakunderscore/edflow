edflow

    .controller("categoriesController", function ($scope, $rootScope, $http) {

        console.log("categoriesController")

        $scope.mainPage = [];
        $scope.selectedCategories = [];
        $scope.subCategories = [];

        $rootScope.selectCategory = function (category) {

            if (category !== "")
                $scope.selectedCategories.push(category);

            else
                $scope.selectedCategories = [];

            $scope.subCategories = [];

            $http.get("api/category?category=" + category.title + "&language=" + $rootScope.language).success(function (data) {

                    //if (data.mainPage.length > 0)
                    //    console.log(data.mainPage[0].title)
                    $rootScope.pages = data.pages;
                    $scope.subCategories = data.subCategories;

                    $scope.moveCategoriesWidth();
                })
                .error(function () {
                    //console.log(e);
                });
        }

        $rootScope.selectCategory("");

        $scope.deselectCategory = function (category) {

            while ($scope.selectedCategories.pop() !== category) {
            }

            if ($scope.selectedCategories.length == 0)
                $scope.selectCategory("");
            else
                $scope.selectCategory($scope.selectedCategories.pop());

        }

        $scope.moveCategoriesWidth = function () {

            var longest = $rootScope.getLongest($scope.selectedCategories.concat($scope.subCategories));
            var menuWidth = $rootScope.getTextWidth(longest.title, "16px Open Sans") + 50;
            var percent = menuWidth / document.body.clientWidth * 100;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            if (percent < 20)
                menuWidth *= 20/percent;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            $rootScope.mWidth = menuWidth + "px";
            $rootScope.cWidth = "calc(100% - " + $rootScope.mWidth + ")";
            //console.log(document.getElementById("menu").offsetWidth)
        }
    })
