edflow

    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.otherwise("/");

        $stateProvider

            .state("news", {
                url: "/news",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("words", {
                url: "/words",
                templateUrl: "assets/view/data/words.html",
                controller: "contentController as content"
            })

            .state("translate", {
                url: "/translate",
                templateUrl: "assets/view/data/words.html",
                controller: "contentController as content"
            })

            .state("wikipedia", {
                url: "/wikipedia",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("book", {
                url: "/book",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("flow", {
                url: "/flow",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

        //$locationProvider.html5Mode(true);
    });
