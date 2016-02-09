edflow

    .controller("categoriesController", function ($scope, $rootScope, $http, ngProgressFactory) {

        var menuItems = [
            {title: "News"},
            {title: "Words"},
            {title: "Translate"}, //частичный перевод текста
            {title: "Wikipedia"}, //навигация по вики
            {title: "Book"},
            {title: "Flow"} //поток новых ссылок из разных источников
        ];

        //console.log("categoriesController")

        //$rootScope.progressbar = ngProgressFactory.createInstance();
        //$rootScope.progressbar.setColor('#619AF9');

        //$scope.mainPage = [];
        $rootScope.selectedCategories = [];
        $scope.subCategories = menuItems;

        $rootScope.selectCategory = function (category) {

            if (category !== "")
                $rootScope.selectedCategories.push(category);

            else
                $rootScope.selectedCategories = [];

            $scope.subCategories = [];

            //$rootScope.progressbar.start();

            var categoryTitle = category.title;

            if (categoryTitle === "Wikipedia")
                categoryTitle = "Main topic classifications";


            $http.get("api/category?category=" + categoryTitle + "&language=" + $rootScope.language).success(function (data) {

                    $rootScope.pages = data.pages;
                    $scope.subCategories = data.subCategories;

                    $scope.moveCategoriesWidth();

                    //$rootScope.progressbar.complete();
                })
                .error(function () {
                });
        }



        //$rootScope.selectCategory("");

        $scope.deselectCategory = function (category) {

            while ($rootScope.selectedCategories.pop() !== category) {
            }

            if ($rootScope.selectedCategories.length == 0)
                $scope.selectCategory("");
            else
                $scope.selectCategory($rootScope.selectedCategories.pop());

        }

        $scope.moveCategoriesWidth = function () {

            var longest = $rootScope.getLongest($rootScope.selectedCategories.concat($scope.subCategories));
            var menuWidth = $rootScope.getTextWidth(longest.title, "16px Open Sans") + 50;
            var percent = menuWidth / document.body.clientWidth * 100;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            if (percent < 20)
                menuWidth *= 20/percent;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            $rootScope.menuWidth = menuWidth + "px";
            $rootScope.contentWidth = "calc(100% - " + $rootScope.menuWidth + ")";
            //console.log(document.getElementById("menu").offsetWidth)
        }
    })
